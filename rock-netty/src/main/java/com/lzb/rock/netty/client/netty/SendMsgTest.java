package com.lzb.rock.netty.client.netty;

import com.lzb.rock.netty.client.util.UtilTest;

public class SendMsgTest {

	public static void main(String[] args) {
		String url = "http://192.168.8.161:19065/tell/netty/push";
		Long startTime = System.currentTimeMillis();
		for (int i = 0; i < 10000; i++) {
			UtilTest.sendOneMsg("body_" + i, "1111", "18589095668", url);
		}
		Long endTime = System.currentTimeMillis();

		System.out.println("耗时：" + (endTime - startTime) + "ms");

	}

}
