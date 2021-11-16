package com.lzb.rock.netty.handler;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.lzb.rock.base.holder.SpringContextHolder;
import com.lzb.rock.base.model.Header;
import com.lzb.rock.base.util.UtilAES;
import com.lzb.rock.base.util.UtilJson;
import com.lzb.rock.base.util.UtilString;
import com.lzb.rock.netty.context.MyNettyContext;
import com.lzb.rock.netty.dto.NettyMsg;
import com.lzb.rock.netty.enums.EventEnum;
import com.lzb.rock.netty.properties.MyNettyProperties;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * I/O数据读写处理类
 */
@Slf4j
public class MyNettyWebSocketChannelInboundHandlerAdapter extends SimpleChannelInboundHandler<Object> {

	private WebSocketServerHandshaker handshaker;

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

		if (msg instanceof FullHttpRequest) {
			handleHttpRequest(ctx, (FullHttpRequest) msg);
			return;
		}

		if (msg instanceof WebSocketFrame) {
			// 处理websocket客户端的消息
			handlerWebSocketFrame(ctx, (WebSocketFrame) msg);
			return;
		}
		log.info("未知的消息：{}", msg.getClass());

		// ReferenceCountUtil.release(msg);
	}

	/**
	 * 客户端与服务端第一次建立连接时 执行
	 *
	 * @param ctx
	 * @throws Exception
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
		String clientIp = insocket.getAddress().getHostAddress();
		// 此处不能使用ctx.close()，否则客户端始终无法与服务端建立连接
		log.info("channelActive建立连接;ip:{};channelId:{}", clientIp, ctx.channel().id());

	}

	/**
	 * 客户端与服务端 断连时 执行
	 *
	 * @param ctx
	 * @throws Exception
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		MyNettyContext.disconnect(ctx);

	}

	/**
	 * 从客户端收到新的数据、读取完成时调用
	 *
	 * @param ctx
	 */
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	/**
	 * 当出现 Throwable 对象才会被调用，即当 Netty 由于 IO 错误或者处理器在处理事件时抛出的异常时
	 *
	 * @param ctx
	 * @param cause
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws IOException {
		// 抛出异常，离线客户端的连接
		MyNettyContext.offLine(ctx);
		AttributeKey<String> accountKey = AttributeKey.valueOf("account");
		log.debug("exceptionCaught:{},channelId:{};cause:{}", ctx.channel().attr(accountKey).get(), ctx.channel().id(),
				cause);
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception, IOException {
		AttributeKey<String> accountKey = AttributeKey.valueOf("account");
		MyNettyContext.offLine(ctx);
		log.info("userEventTriggered:account:{},channelId:{};evt:{}", ctx.channel().attr(accountKey).get(),
				ctx.channel().id(), evt);
		super.userEventTriggered(ctx, evt);
	}

	/**
	 * 从客户端收到新的数据时，这个方法会在收到消息时被调用，websocket客户端的消息
	 *
	 * @param ctx
	 * @param msg
	 */
	private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
		/**
		 * 判断是否关闭链路的指令
		 */
		if (frame instanceof CloseWebSocketFrame) {
			log.info("handlerWebSocketFrame,收到关闭链路指令;account:{};channelId:{}", ctx.name(), ctx.channel().id());
			// MyNettyContext.disconnect(account);
			handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
			return;
		}
		// 判断是否ping消息
		if (frame instanceof PingWebSocketFrame) {
			ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
			return;
		}
		// 仅支持文本消息，不支持二进制消息
		if (!(frame instanceof TextWebSocketFrame)) {
			log.error("仅支持文本消息，不支持二进制消息:{}", frame.getClass().getName());
			return;
		}
		if (frame instanceof TextWebSocketFrame) {
			AttributeKey<String> accountKey = AttributeKey.valueOf("account");
			// 返回应答消息
			String request = ((TextWebSocketFrame) frame).text();
			// log.debug("服务端收到：" + request);
			TextWebSocketFrame tws = new TextWebSocketFrame(request);
			String text = tws.text();
			log.debug("收到消息;account:{};channelId:{};text：{}", ctx.channel().attr(accountKey).get(), ctx.channel().id(),
					text);
			if (UtilString.isNotBlank(text) && UtilJson.isJsonString(text)) {
				NettyMsg nettyMsg = UtilJson.getJavaBean(text, NettyMsg.class);
				if (nettyMsg.getEvent().equals(EventEnum.HEART.getCode())) {
					NettyMsg nettyMsg2 = new NettyMsg();
					nettyMsg2.setBody(nettyMsg.getBody());
					nettyMsg2.setEvent(EventEnum.HEART_NOT.getCode());
					MyNettyContext.writeAndFlush(ctx, nettyMsg2);
					MyNettyContext.onLine(ctx);
					log.debug("回应心跳:account:{},channelId:{},msg:{},", ctx.channel().attr(accountKey).get(),
							ctx.channel().id(), nettyMsg2);
				} else if (nettyMsg.getEvent().equals(EventEnum.HEART_NOT.getCode())) {

				} else if (nettyMsg.getEvent().equals(EventEnum.ACK_MSG.getCode())) {
					MyNettyContext.addAck(nettyMsg.getBody());
				} else {
					log.info("未知event消息：{}", text);
				}
			} else {
				log.info("请求数据格式异常：{}", text);
			}
		} else {
			log.error("未知消息:getName{};frame:{}", frame.getClass().getName(), frame);
		}

	}

	/**
	 * 唯一的一次http请求，用于创建websocket
	 */
	private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
		// 要求Upgrade为websocket，过滤掉get/Post
		if (!req.decoderResult().isSuccess() || (!"websocket".equalsIgnoreCase(req.headers().get("Upgrade")))) {
			// 若不是websocket方式，则创建BAD_REQUEST的req，返回给客户端
			log.info("handleHttpRequest,请求不合法BAD_REQUEST 返回;isSuccess:{};Upgrade:{};uri:{}",
					req.decoderResult().isSuccess(), req.headers().get("Upgrade"), req.uri());
			sendHttpResponse(ctx, req,
					new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
			return;
		}
		String token = req.headers().get("token");
		log.debug("handleHttpRequest 收到消息,token:{}", token);
		if (UtilString.isBlank(token)) {
			log.info("handleHttpRequest;非法连接:{}", req.uri());
			ctx.disconnect();
			return;
		}

		MyNettyProperties myNettyProperties = SpringContextHolder.getBean(MyNettyProperties.class);
		String str = UtilAES.Base64AESDncode(myNettyProperties.getAesEncodeRules(), token);
		if (UtilString.isBlank(str)) {
			log.info("handleHttpRequest;非法连接:{}", req.uri());
			ctx.disconnect();
			return;
		}

		Header header = UtilJson.getJavaBean(str, Header.class);
		if (header == null) {
			log.info("handleHttpRequest;非法连接:{};str:{}", req.uri(), str);
			ctx.disconnect();
			return;
		}
		String account = header.getUserId();
		if (UtilString.isBlank(account)) {
			log.info("handleHttpRequest;非法连接:{};str:{}", req.uri(), str);
			ctx.disconnect();
			return;
		}
		// 建立连接
		WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(getWebSocketLocation(req),
				null, false);
		handshaker = wsFactory.newHandshaker(req);
		if (handshaker == null) {
			WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
		} else {
			handshaker.handshake(ctx.channel(), req);
		}
		MyNettyContext.bind(account, ctx);
		log.info("绑定成功;account:{};id:{}", account, ctx.channel().id());
	}

	private static String getWebSocketLocation(HttpRequest req) {
		MyNettyProperties myNettyProperties = SpringContextHolder.getBean(MyNettyProperties.class);

		String location = req.headers().get(HttpHeaderNames.HOST) + myNettyProperties.getWebSocketPath();
		boolean sslFlag = false;
		if (sslFlag) {
			return "wss://" + location;
		} else {
			return "ws://" + location;
		}
	}

	/**
	 * 拒绝不合法的请求，并返回错误信息
	 */
	private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, DefaultFullHttpResponse res) {
		// 返回应答给客户端
		if (res.status().code() != 200) {
			ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
			res.content().writeBytes(buf);
			buf.release();
		}
		ChannelFuture f = ctx.channel().writeAndFlush(res);
		// 如果是非Keep-Alive，关闭连接
//		if (!isKeepAlive(req) || res.status().code() != 200) {
//			f.addListener(ChannelFutureListener.CLOSE);
//		}
	}
}
