package com.lzb.rock.netty.client;

import org.slf4j.LoggerFactory;

import com.lzb.rock.base.util.UtilDate;
import com.lzb.rock.base.util.UtilString;
import com.lzb.rock.netty.client.util.UtilTest;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;

public class TestSendMsg {

	/**
	 * 测试http 发送消息类
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		ch.qos.logback.classic.Logger logger = loggerContext.getLogger("root");
		logger.setLevel(Level.INFO);

		String url = "http://127.0.0.1";
		String webSocketUrl = url + ":19066/websocket";
		String webUrl = url + ":19065";

		// 测试点对点消息
		for (int i = 0; i < 1; i++) {
			UtilTest.sendOneMsg(
					"测试点对点消息_" + i + "===>" + UtilString.getRandomString(64) + "_"
							+ UtilDate.getFomtDateByThisTime("yyy-MM-dd HH:mm:ss"),
					"888", "8888", webUrl + "/tell/netty/push");
		}

	}

}
