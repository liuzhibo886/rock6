package com.lzb.rock.netty.context;

import java.util.concurrent.DelayQueue;

import com.lzb.rock.netty.dto.MyDelayed;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DelayContext {
	private final static DelayQueue<MyDelayed> delayQueue = new DelayQueue<MyDelayed>();

	/**
	 * 获取到时的延迟消息
	 * 
	 * @return
	 */
	public static MyDelayed take() {
		MyDelayed myDelayed = null;

		try {
			myDelayed = delayQueue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return myDelayed;
	}

	/**
	 * 增加延迟消息
	 * 
	 * @param myDelayed
	 */

	public static void offer(MyDelayed myDelayed) {
		if (myDelayed == null || myDelayed.getNettyMsg() == null) {
			return;
		}
		if (myDelayed.getNettyMsg().getCount() < 1) {
			return;
		}
		if (myDelayed.getNettyMsg().getDueTime() < 1) {
			return;
		}

		myDelayed.rest();
		
		myDelayed.getNettyMsg().setCount(myDelayed.getNettyMsg().getCount() - 1);

		delayQueue.offer(myDelayed);
	}

}
