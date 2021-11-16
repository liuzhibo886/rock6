package com.lzb.rock.netty.client.netty;

import com.lzb.rock.base.util.UtilJson;
import com.lzb.rock.base.util.UtilString;
import com.lzb.rock.netty.dto.NettyMsg;
import com.lzb.rock.netty.enums.EventEnum;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketHandshakeException;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @description:
 * @author: Mr.Joe
 * @create:
 */
@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<Object> {

	WebSocketClientHandshaker handshaker;

	ChannelPromise handshakeFuture;

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) {
		this.handshakeFuture = ctx.newPromise();
	}

	public ChannelFuture handshakeFuture() {
		return this.handshakeFuture;
	}

	public void setHandshaker(WebSocketClientHandshaker handshaker) {
		this.handshaker = handshaker;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		Channel ch = ctx.channel();
		FullHttpResponse response;
		if (!this.handshaker.isHandshakeComplete()) {
			try {
				response = (FullHttpResponse) msg;
				// 握手协议返回，设置结束握手
				this.handshaker.finishHandshake(ch, response);
				// 设置成功
				this.handshakeFuture.setSuccess();
			} catch (WebSocketHandshakeException var7) {
				FullHttpResponse res = (FullHttpResponse) msg;
				String errorMsg = String.format("WebSocket Client failed to connect,status:%s,reason:%s", res.status(),
						res.content().toString(CharsetUtil.UTF_8));
				this.handshakeFuture.setFailure(new Exception(errorMsg));
			}

			return;
		}

		if (msg instanceof FullHttpResponse) {
			response = (FullHttpResponse) msg;
			// this.listener.onFail(response.status().code(),
			// response.content().toString(CharsetUtil.UTF_8));
			throw new IllegalStateException("Unexpected FullHttpResponse (getStatus=" + response.status() + ", content="
					+ response.content().toString(CharsetUtil.UTF_8) + ')');

		}

		if (!(msg instanceof WebSocketFrame)) {
			log.info("未知消息类型==>{}", msg.getClass());
		}
		WebSocketFrame frame = (WebSocketFrame) msg;

		if (frame instanceof TextWebSocketFrame) {
			TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;

			String message = textFrame.text();
			if (UtilString.isBlank(message)) {
				return;
			}
			AttributeKey<String> accountKey = AttributeKey.valueOf("account");
			String account = ch.attr(accountKey).get();

			NettyMsg nettyMsg = UtilJson.getJavaBean(message, NettyMsg.class);

			if (nettyMsg != null) {
				if ("HEART".equals(nettyMsg.getEvent()) || "HEART_NOT".equals(nettyMsg.getEvent())) {
					NettyMsg heartNot = new NettyMsg(EventEnum.HEART_NOT.getCode(), null, "sys", "");
					ch.writeAndFlush(new TextWebSocketFrame(UtilJson.getStr(heartNot)));
				} else {
					NettyMsg ack = new NettyMsg();
					ack.setEvent(EventEnum.ACK_MSG.getCode());
					ack.setBody(nettyMsg.getMsgId());
					ch.writeAndFlush(new TextWebSocketFrame(UtilJson.getStr(ack)));
					log.debug("用户:{}，收到消息:{}", account, message);
				}
			}
			return;
		}
		if (frame instanceof BinaryWebSocketFrame) {
			BinaryWebSocketFrame binFrame = (BinaryWebSocketFrame) frame;
			log.info("BinaryWebSocketFrame==>");
			return;
		}

		if (frame instanceof PongWebSocketFrame) {
			log.info("PongWebSocketFrame==>");
			return;
		}

		if (frame instanceof CloseWebSocketFrame) {
			log.info("关闭连接消息");
			ch.close();
			return;
		}

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		super.channelRead(ctx, msg);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		log.info("channelActive==>{}", ctx.channel().remoteAddress());
		super.channelActive(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.info("exceptionCaught==>{}", ctx.channel().remoteAddress());
		super.exceptionCaught(ctx, cause);
	}
}
