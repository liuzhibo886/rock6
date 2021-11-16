package com.lzb.rock.netty.dto;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class MyDelayed implements Delayed {

	NettyMsg nettyMsg;

	Long duration;

	/**
	 * @param nettyMsg
	 * @param duration 超时时间 秒
	 */
	public MyDelayed(NettyMsg nettyMsg) {
		this.nettyMsg = nettyMsg;
	}

	public void rest() {
		this.duration = System.currentTimeMillis() + this.nettyMsg.getDueTime();
	}

	public NettyMsg getNettyMsg() {

		return this.nettyMsg;
	}

	public Long getDueTime() {

		return this.duration;
	}

	@Override
	public int compareTo(Delayed o) {
		Integer flag = 0;
		long compare = this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS);
		if (compare > 0) {
			flag = 1;
		}
		if (compare < 0) {
			flag = -1;
		}

		return flag;
	}

	@Override
	public long getDelay(TimeUnit unit) {

		return unit.convert(this.duration - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
	}

}
