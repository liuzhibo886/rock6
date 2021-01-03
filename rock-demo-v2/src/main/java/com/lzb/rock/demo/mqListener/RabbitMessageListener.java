package com.lzb.rock.demo.mqListener;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * RabbitMQ 消息队列消费者
 * @author lzb
 *
 *2020年12月22日 下午4:59:15
 *
 */
@Component
@Slf4j
public class RabbitMessageListener {

	@RabbitHandler
	@RabbitListener(bindings = {
			@QueueBinding(value = @Queue(value = "TEST_0_QUEUE", durable = "true"), exchange = @Exchange(value = "BZCST0"), key = "TEST_DEMO") })
	public void test0(Map testMessage) {
		log.info("RabbitListener，TEST_0_QUEUE 消费者收到消息  : {}", testMessage);
	}

	@RabbitHandler
	@RabbitListener(bindings = {
			@QueueBinding(value = @Queue(value = "TEST_1_QUEUE", durable = "true"), exchange = @Exchange(value = "BZCST"), key = "TEST_DEMO") })
	public void test1(Map testMessage) {
		log.info("RabbitListener，TEST_1_QUEUE 消费者收到消息  : {}", testMessage);
	}
	
	@RabbitHandler
	@RabbitListener(bindings = {
			@QueueBinding(value = @Queue(value = "TEST_1_QUEUE", durable = "true"), exchange = @Exchange(value = "BZCST"), key = "TEST_DEMO") })
	public void test2(Map testMessage) {
		log.info("RabbitListener，TEST_2_QUEUE 消费者收到消息  : {}", testMessage);
	}

}
