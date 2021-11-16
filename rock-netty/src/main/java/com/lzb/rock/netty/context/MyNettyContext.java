package com.lzb.rock.netty.context;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

import com.lzb.rock.base.enums.RockEnum;
import com.lzb.rock.base.facade.IEnum;
import com.lzb.rock.base.fileter.BloomFileter;
import com.lzb.rock.base.util.UtilJson;
import com.lzb.rock.base.util.UtilString;
import com.lzb.rock.netty.dto.NettyMsg;
import com.lzb.rock.netty.enums.NettyEnum;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import io.netty.util.internal.PlatformDependent;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户ChannelHandlerContext对象
 *
 * @author lzb
 *
 * @date 2019年9月30日 下午11:13:36
 */
@Slf4j
public class MyNettyContext {

	/**
	 * 在线用户，账号为key
	 */
	private final static ConcurrentMap<String, ChannelHandlerContext> accountGroup = PlatformDependent
			.newConcurrentHashMap();

	/**
	 * 离线用户
	 */
	private final static ConcurrentMap<String, ChannelHandlerContext> offLineAccountGroup = PlatformDependent
			.newConcurrentHashMap();
	/**
	 * 消息ACK,ack1 满了后，启用ack2 ，30分钟后重置ack1,
	 */
	private final static BloomFileter ack1 = new BloomFileter(10000 * 10000 * 100);

	private final static BloomFileter ack2 = new BloomFileter(10000 * 10000 * 100);

	/**
	 * 切换ack的使用率，注意，切换的时候，备用ack数据会被清空
	 */
	private final static double autoSwitchRate = 0.8;

	/**
	 * 定时检测,使用ack1 时为true
	 */
	private static AtomicBoolean isAck1 = new AtomicBoolean(true);

	/**
	 * 检测
	 */
	public static void checkAck() {
		// 判断当前使用率
		double useRate = 0;
		if (isAck1.get()) {
			useRate = ack1.getUseRate();
		} else {
			useRate = ack2.getUseRate();
		}

		if (useRate >= autoSwitchRate) {
			if (isAck1.get()) {
				ack2.clear();
				isAck1.set(false);
				log.info("BloomFileter 切换为 ack2");
				return;
			}
			ack1.clear();
			isAck1.set(true);
			log.info("BloomFileter 切换为 ack1");
		}

	};

	/**
	 * ACK 上报
	 * 
	 * @param msgId
	 */
	public static void addAck(String msgId) {
		if (UtilString.isBlank(msgId)) {
			return;
		}

		if (isAck1.get()) {
			ack1.add(msgId);
			return;
		}
		ack2.add(msgId);
	}

	/**
	 * 已上报返回true
	 * 
	 * @param msgId
	 * @return
	 */
	public static Boolean isAck(String msgId) {
		Boolean flag = false;

		flag = ack1.check(msgId);
		if (!flag) {
			flag = ack2.check(msgId);
		}

		return flag;
	}

	public static ConcurrentMap<String, ChannelHandlerContext> getAccountGroup() {

		return accountGroup;
	}

	/**
	 * 用户离线
	 * 
	 * @param ctx
	 */
	public static void offLine(ChannelHandlerContext ctx) {
		AttributeKey<String> accountKey = AttributeKey.valueOf("account");
		String account = ctx.channel().attr(accountKey).get();
		if (UtilString.isBlank(account)) {
			disconnect(ctx);
			return;
		}
		offLineAccountGroup.put(account, ctx);
		accountGroup.remove(account);

	}

	/**
	 * 用户上线
	 * 
	 * @param ctx
	 */
	public static void onLine(ChannelHandlerContext ctx) {

		AttributeKey<String> accountKey = AttributeKey.valueOf("account");
		String account = ctx.channel().attr(accountKey).get();
		if (UtilString.isBlank(account)) {
			disconnect(ctx);
			return;
		}
		accountGroup.put(account, ctx);
		offLineAccountGroup.remove(account);

	}

	/*
	 * 根据账号获取绑定的ctx
	 */
	public static ChannelHandlerContext getCtxByAccount(String account) {

		return accountGroup.get(account);
	}

	/**
	 * 账号绑定
	 * 
	 * @param account
	 * @param ctx
	 */
	public static void bind(String account, ChannelHandlerContext ctx) {
		if (ctx == null) {
			log.info("ctx为空;account:{}", account);
			return;
		}
		if (UtilString.isBlank(account)) {
			log.info("account为空;channelId:{}", ctx.channel().id());
			return;
		}

		AttributeKey<String> accountKey = AttributeKey.valueOf("account");
		ctx.channel().attr(accountKey).set(account);
		accountGroup.put(account, ctx);
	}

	/**
	 * 断开连接
	 * 
	 * @param account
	 */
	public static void disconnect(String account) {
		ChannelHandlerContext ctx = accountGroup.get(account);
		if (ctx != null) {
			ctx.channel().disconnect();
			accountGroup.remove(account);
		}
	}

	public static void disconnect(ChannelHandlerContext ctx) {
		AttributeKey<String> accountKey = AttributeKey.valueOf("account");
		String account = ctx.channel().attr(accountKey).get();
		if (UtilString.isNotBlank(account)) {
			accountGroup.remove(account);
		}
		ctx.channel().disconnect();
	}

	/**
	 * 删除绑定关系
	 * 
	 * @param account
	 */
	public static void removeBind(String account) {
		accountGroup.remove(account);
	}

	public static IEnum writeAndFlush(NettyMsg msg) {
		ChannelHandlerContext ctx = accountGroup.get(msg.getReceiveAccount());
		if (ctx == null) {
			// 用户不在线,后面在处理
			log.info("用户通道不存在==>{}", UtilJson.getStrByFormat(msg));
			return NettyEnum.CTX_NOT;
		}
		writeAndFlush(ctx, msg);
		return RockEnum.SUCCESS;
	}

	/**
	 * 发送消息
	 * 
	 * @param ctx
	 * @param msg
	 */
	public static void writeAndFlush(ChannelHandlerContext ctx, NettyMsg msg) {
		ctx.channel().writeAndFlush(new TextWebSocketFrame(UtilJson.getStr(msg)));
	}
}
