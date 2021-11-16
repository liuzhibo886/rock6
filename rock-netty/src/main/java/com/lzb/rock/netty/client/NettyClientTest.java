package com.lzb.rock.netty.client;

import java.net.URI;

import com.lzb.rock.base.model.Header;
import com.lzb.rock.base.util.UtilAES;
import com.lzb.rock.base.util.UtilJson;
import com.lzb.rock.netty.client.netty.NettyClient;
import com.lzb.rock.netty.dto.NettyMsg;
import com.lzb.rock.netty.enums.EventEnum;

import ch.qos.logback.classic.LoggerContext;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyClientTest {

	/**
	 * 基于netty websocket client
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		ch.qos.logback.classic.Logger logger = loggerContext.getLogger("root");
		logger.setLevel(Level.INFO);

		String encodeRules = "c5xwYYiSgTkDfDemuULJ9zWto8pqiXASbxKxR7pmdU6LXTq7v2Et9NF8ZRI3jvgK395zZ0c9vWPxNCG5FwBAqy2zROX05Kizl39";
		NettyClient nettyClient = new NettyClient();
		String host = "192.168.8.161";
		Integer port = 19066;

		URI uri = new URI("ws://" + host + ":" + port + "/websocket");

		for (int i = 0; i < 1000; i++) {
			String account = "18589095668_" + i;
			Header header = new Header();
			header.setUserId(account);
			String token = UtilAES.Base64AESEncode(encodeRules, UtilJson.getStr(header));
			ChannelFuture channelFuture = nettyClient.connect(uri, token);
			// 建立连接
			// 获取Channel
			Channel channel = channelFuture.channel();
			AttributeKey<String> accountKey = AttributeKey.valueOf("account");
			channel.attr(accountKey).set(account);
		}

//		NettyMsg msg = new NettyMsg();
//		msg.setBody("99999");
//		msg.setEvent(EventEnum.TEXT_MSG.getCode());
//		for (int i = 0; i < 10000; i++) {
//			channel.writeAndFlush(new TextWebSocketFrame(UtilJson.getStr(msg)));
//			Thread.sleep(1000);
//		}

		// 线程阻塞直到连接关闭
//		channel.closeFuture().sync();

	}

}
