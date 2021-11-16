package com.lzb.rock.netty.client;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.LoggerFactory;

import com.lzb.rock.base.util.UtilJson;
import com.lzb.rock.netty.client.util.UtilTest;
import com.lzb.rock.netty.dto.NettyMsg;
import com.lzb.rock.netty.enums.EventEnum;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class MyWebSocketClient extends WebSocketClient {

	String account;

	public MyWebSocketClient(URI serverUri, Map<String, String> httpHeaders, String account) {
		super(serverUri, httpHeaders);
		this.account = account;
	}

	@Override
	public void onOpen(ServerHandshake handshakedata) {
		log.info("打开连接:{}", account);
	}

	@Override
	public void onMessage(String message) {
		NettyMsg msg = UtilJson.getJavaBean(message, NettyMsg.class);
		if (msg != null) {
			if ("HEART".equals(msg.getEvent()) || "HEART_NOT".equals(msg.getEvent())) {
				msg.setEvent(EventEnum.HEART_NOT.getCode());
				this.send(UtilJson.getStr(msg));
			} else {
				NettyMsg ack = new NettyMsg();
				ack.setEvent(EventEnum.ACK_MSG.getCode());
				ack.setBody(msg.getMsgId());
				this.send(UtilJson.getStr(ack));
				log.info("用户:{}，收到消息:{}", account, UtilJson.getStrByFormat(msg));
			}
		}
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		log.info("关闭连接：{}", account);

	}

	@Override
	public void onError(Exception ex) {
		log.info("异常：{};ex:{}", account, ex);
	}

	public static void main(String[] args) throws Exception {
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		ch.qos.logback.classic.Logger logger = loggerContext.getLogger("root");
		logger.setLevel(Level.INFO);

		String url = "http://lzb01.liuzhibo.top";
		String webSocketUrl = url + ":19066/websocket";
		String webUrl = url + ":19065";
		// 用户1
		String account1 = "666666";
		String aesAppToken1 = UtilTest.getAesAppToken(account1);
		Map<String, String> httpHeaders1 = new HashMap<String, String>();
		httpHeaders1.put("token", aesAppToken1);
		MyWebSocketClient socketClient1 = new MyWebSocketClient(new URI(webSocketUrl), httpHeaders1, account1);
		socketClient1.connect();
		// 用户2
		String account2 = "8888";
		String aesAppToken2 = UtilTest.getAesAppToken(account2);
		Map<String, String> httpHeaders2 = new HashMap<String, String>();
		httpHeaders2.put("token", aesAppToken2);
		MyWebSocketClient socketClient2 = new MyWebSocketClient(new URI(webSocketUrl), httpHeaders2, account2);
		socketClient2.connect();

		while (true) {
			System.out.println("输入 用户：" + account1 + "给" + account2 + "发送消息内容==>");
			Scanner input = new Scanner(System.in);
			// 接受String类型
			String str = input.next();
			// 输出结果
			if ("quit@".equals(str)) {
				break;
			}
			UtilTest.sendOneMsg(str, account1, account2, webUrl + "/tell/netty/push");
		}

	}

}