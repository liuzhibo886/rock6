package com.lzb.rock.netty.handler;

import java.util.concurrent.TimeUnit;

import com.lzb.rock.netty.context.MyNettyContext;
import com.lzb.rock.netty.dto.NettyMsg;
import com.lzb.rock.netty.enums.EventEnum;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * check idle chanel.
 *
 * @author fengfei
 *
 */
@Slf4j
public class MyIdleStateHandler extends IdleStateHandler {

	public static final NettyMsg HEART = new NettyMsg(EventEnum.HEART.getCode(), null, "sys", "");

	public MyIdleStateHandler(int readerIdleTimeSeconds, int writerIdleTimeSeconds, int allIdleTimeSeconds) {
		super(readerIdleTimeSeconds, writerIdleTimeSeconds, allIdleTimeSeconds);
	}

	public MyIdleStateHandler(int readerIdleTimeSeconds, int writerIdleTimeSeconds, int allIdleTimeSeconds,
			TimeUnit unit) {
		super(readerIdleTimeSeconds, writerIdleTimeSeconds, allIdleTimeSeconds, unit);
	}

	@Override
	protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {

		/**
		 * 检测到离线，主动发送心跳
		 */

		/**
		 * 写空闲
		 */
		if (IdleState.WRITER_IDLE == evt.state()) {
		//	MyNettyContext.writeAndFlush(ctx, HEART);
		} else if (IdleState.READER_IDLE == evt.state()) {
			// 读空闲
			MyNettyContext.writeAndFlush(ctx, HEART);
		} else if (IdleState.ALL_IDLE == evt.state()) {
			// 读写空闲
			super.channelIdle(ctx, evt);
		}
	}
}
