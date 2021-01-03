package com.lzb.rock.demo.mqListener;

import java.util.List;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import com.lzb.rock.base.aop.annotation.Log;
import com.lzb.rock.base.aop.annotation.MqProperties;
import com.lzb.rock.base.facade.IMqEnum;
import com.lzb.rock.base.util.UtilDate;
import com.lzb.rock.base.util.UtilString;
import com.lzb.rock.demo.enums.DemoMqEnum;
import com.lzb.rock.rocketmq.facade.IRocketMqMessageListenerConcurrently;

import lombok.extern.slf4j.Slf4j;

/**
 * 并发消费消息
 * 
 * @author lzb
 * @date 2020年9月17日下午5:53:29
 */
@Slf4j
@Component
@MqProperties(consumeThreadMin = 30, consumerConsumeThreadMax = 100, consumeMessageBatchMaxSiz = 100)
public class MyConcurrentlyMessageListener implements IRocketMqMessageListenerConcurrently {

	@Override
	@Log(before = false, end = false)
	public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {

		for (MessageExt messageExt : msgs) {
			String message = UtilString.toString(messageExt.getBody());
			if (UtilString.isBlank(message)) {
				continue;
			}
			log.info("收到消息,topic:{};tags:{},msgId:{},message:{},time:{}", messageExt.getTopic(), messageExt.getTags(),
					messageExt.getMsgId(), message,UtilDate.getFomtTimeByDateString());
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

	@Override
	public IMqEnum getMqEnum() {
		return DemoMqEnum.DEMO_TEST;
	}

}
