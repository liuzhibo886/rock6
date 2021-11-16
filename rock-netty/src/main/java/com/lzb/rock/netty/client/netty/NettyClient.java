package com.lzb.rock.netty.client.netty;

import java.net.URI;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.cookie.ClientCookieDecoder;
import io.netty.handler.codec.http.cookie.DefaultCookie;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

/**
 * https://blog.csdn.net/u010939285/article/details/81231221
 * 
 * @author lzb
 *
 */
@Slf4j
public class NettyClient {

	private EventLoopGroup group = null;

	Bootstrap bootstrap = null;

	public NettyClient() {
		group = new NioEventLoopGroup(8);

		this.bootstrap = new Bootstrap();
		this.bootstrap = bootstrap.group(group).channel(NioSocketChannel.class)
				.handler(new NettyClientChannelInitializer());
	}

	public ChannelFuture connect(URI uri, String token) throws Exception {
		HttpHeaders httpHeaders = new DefaultHttpHeaders();
		httpHeaders.set("token", token);
		WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory.newHandshaker(uri, WebSocketVersion.V13,
				(String) null, true, httpHeaders);
		ChannelFuture channelFuture = bootstrap.connect(uri.getHost(), uri.getPort()).sync();

		NettyClientHandler handler = (NettyClientHandler) channelFuture.channel().pipeline().get("handler");
		handler.setHandshaker(handshaker);
		handshaker.handshake(channelFuture.channel());

		handler.handshakeFuture().sync();
//		HttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, uri.getRawPath());
//		request.headers().set(HttpHeaderNames.HOST, uri.getHost() + "/websocket");
//		request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
//		request.headers().set(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.GZIP);
//		request.headers().set(HttpHeaderNames.UPGRADE, HttpHeaderValues.WEBSOCKET);
//		request.headers().set("token", token);
//		DefaultCookie cookie = new DefaultCookie("my-cookie", "foo");
//		DefaultCookie cookie2 = new DefaultCookie("another-cookie", "bar");
//		// ClientCookieDecoder.STRICT.decode(aa.domain());
//		request.headers().set(HttpHeaderNames.COOKIE, cookie);
//		request.headers().set(HttpHeaderNames.COOKIE, cookie2);
//
//		channelFuture.channel().writeAndFlush(request);
		return channelFuture;
	}

	public void shutdownClient() {
		// 等待数据的传输通道关闭
		group.shutdownGracefully();
	}

}
