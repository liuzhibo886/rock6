package com.lzb.rock.netty.client.util;

import com.lzb.rock.base.model.Header;
import com.lzb.rock.base.util.UtilAES;
import com.lzb.rock.base.util.UtilHttp;
import com.lzb.rock.base.util.UtilJson;
import com.lzb.rock.netty.dto.NettyMsg;
import com.lzb.rock.netty.enums.EventEnum;

public class UtilTest {
	private static String encodeRules = "c5xwYYiSgTkDfDemuULJ9zWto8pqiXASbxKxR7pmdU6LXTq7v2Et9NF8ZRI3jvgK395zZ0c9vWPxNCG5FwBAqy2zROX05Kizl39";

	public static String getAesAppToken(String account) {

		return getAesAppToken(null, account, null, null, null);
	}

	public static String getAesAppToken(String token, String account, String platform, String version,
			String deviceId) {
		Header header = new Header();
		header.setToken(token);
		header.setUserId(account);
		header.setPlatform(platform);
		header.setVersion(version);
		header.setDeviceId(deviceId);
		String aesAppToken = UtilAES.Base64AESEncode(encodeRules, UtilJson.getStr(header));
		return aesAppToken;
	}

	public static void sendOneMsg(String body, String sendAccount, String receiveAccount, String url) {
		NettyMsg msg = new NettyMsg();
		msg.setEvent(EventEnum.TEXT_MSG.getCode());
		msg.setBody(body);
		msg.setSendAccount(sendAccount);
		msg.setReceiveAccount(receiveAccount);
		UtilHttp.postBody(url, UtilJson.getStr(msg));
	}
}
