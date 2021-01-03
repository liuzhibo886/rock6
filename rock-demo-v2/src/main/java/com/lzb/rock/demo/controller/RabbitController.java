package com.lzb.rock.demo.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * rabbitMq 消息发送
 * 
 * @author lzb
 *
 *         2020年12月22日 下午5:05:02
 *
 */
@RestController
@RequestMapping(value = "/rabbit")
public class RabbitController {

	@Autowired
	RabbitTemplate rabbitTemplate; // 使用RabbitTemplate,这提供了接收/发送等等方法

	@GetMapping("/sendMessage")
	public String sendMessage() {
		String messageId = String.valueOf(UUID.randomUUID());
		String messageData = "test message, hello!";
		String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		Map<String, Object> map = new HashMap<>();
		map.put("messageId", messageId);
		map.put("messageData", messageData);
		map.put("createTime", createTime);
		// 将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
		rabbitTemplate.convertAndSend("BZCST0", "TEST_DEMO", map);
		return "ok";
	}

}
