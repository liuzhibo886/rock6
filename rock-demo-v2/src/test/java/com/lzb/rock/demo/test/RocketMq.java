package com.lzb.rock.demo.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.lzb.rock.base.enums.DelayLevelEnum;
import com.lzb.rock.base.facade.IMqMapper;
import com.lzb.rock.base.util.UtilDate;
import com.lzb.rock.base.util.UtilString;
import com.lzb.rock.demo.DemoApplication;
import com.lzb.rock.demo.enums.DemoMqEnum;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
@Slf4j
public class RocketMq {

	@Autowired
	IMqMapper mqMapper;

	/**
	 * 发送普通消息
	 * @throws InterruptedException 
	 */
	@Test
	public void sendMsg() throws InterruptedException {
		String body = "demo测试普通消息==>"+UtilDate.getFomtTimeByDateString();
		String msgId = mqMapper.sendMsg(body, DemoMqEnum.DEMO_TEST);
		if (UtilString.isNotBlank(msgId)) {
			log.info("消息发送成功;msgid:{};body:{}", msgId, body);
		} else {
			log.error("消息发送失败,body:{}", body);
		}

		Thread.sleep(2000);
	}
	
	/**
	 * 发送任意延时消息，需配合rock-delay模块使用
	 * @throws InterruptedException
	 */
	
	@Test
	public void sendDelayMsg() throws InterruptedException {
		String body = "demo任意时间间隔延迟消息==>"+UtilDate.getFomtTimeByDateString();
		String msgId = mqMapper.sendDelayMsg(body, DemoMqEnum.DEMO_TEST, 3500L);
		if (UtilString.isNotBlank(msgId)) {
			log.info("消息发送成功;msgid:{};body:{}", msgId, body);
		} else {
			log.error("消息发送失败,body:{}", body);
		}

		Thread.sleep(8000);
	}
	
	/**
	 * 发送延时消息，延迟间隔为rocketMq间隔
	 * @throws InterruptedException
	 */
	@Test
	public void sendDelayMsgByDelayLevel() throws InterruptedException {
		String body = "demo固定时间间隔延迟消息==>"+UtilDate.getFomtTimeByDateString();
		String msgId = mqMapper.sendDelayMsg(body, DemoMqEnum.DEMO_TEST, DelayLevelEnum.DELLAY_5s);
		if (UtilString.isNotBlank(msgId)) {
			log.info("消息发送成功;msgid:{};body:{}", msgId, body);
		} else {
			log.error("消息发送失败,body:{}", body);
		}

		Thread.sleep(8000);
	}
	
	
	
}
