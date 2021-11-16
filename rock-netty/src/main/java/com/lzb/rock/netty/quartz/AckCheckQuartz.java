package com.lzb.rock.netty.quartz;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lzb.rock.netty.context.MyNettyContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Async
public class AckCheckQuartz {

	@Scheduled(cron = "${ack.check.cron:0/10 * * * * ?}")
	public void run() throws Exception {
		MyNettyContext.checkAck();
	}

}
