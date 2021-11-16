package com.lzb.rock.netty.threads;

import com.lzb.rock.netty.context.DelayContext;
import com.lzb.rock.netty.context.MyNettyContext;
import com.lzb.rock.netty.dto.MyDelayed;

import lombok.extern.slf4j.Slf4j;

/**
 * 守护进程
 * 
 * @author lzb
 *
 */
@Slf4j
public class DelayThread extends Thread {

	@Override
	public void run() {
		while (true) {
			MyDelayed myDelayed = DelayContext.take();
			if (myDelayed == null) {
				continue;
			}
			if (MyNettyContext.isAck(myDelayed.getNettyMsg().getMsgId())) {
				return;
			}
			//此处可增加线程池处理
			MyNettyContext.writeAndFlush(myDelayed.getNettyMsg());
			DelayContext.offer(myDelayed);

		}

	}

}
