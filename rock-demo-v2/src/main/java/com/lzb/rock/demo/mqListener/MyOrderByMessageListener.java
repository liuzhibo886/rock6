package com.lzb.rock.demo.mqListener;

import java.util.List;

import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import com.lzb.rock.base.aop.annotation.MqProperties;
import com.lzb.rock.base.facade.IMqEnum;
import com.lzb.rock.base.util.UtilDate;
import com.lzb.rock.base.util.UtilString;
import com.lzb.rock.demo.enums.DemoMqEnum;
import com.lzb.rock.rocketmq.facade.IRocketMqMessageListenerOrderBy;

import lombok.extern.slf4j.Slf4j;

/**
 * 顺序消费，只能单线程，否则顺序消费没意义
 * 
 * @author zhiboliu
 *
 */
@Slf4j
@Component
@MqProperties(consumeThreadMin = 1, consumerConsumeThreadMax = 1, consumeMessageBatchMaxSiz = 1)
public class MyOrderByMessageListener implements IRocketMqMessageListenerOrderBy {

	@Override
	public IMqEnum getMqEnum() {
		return DemoMqEnum.DEMO_ORDER_BY;
	}

	@Override
	public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {

		for (MessageExt messageExt : msgs) {
			String message = UtilString.toString(messageExt.getBody());
			if (UtilString.isBlank(message)) {
				continue;
			}
			log.info("收到消息,topic:{};tags:{},msgId:{},message:{},time:{}", messageExt.getTopic(), messageExt.getTags(),
					messageExt.getMsgId(), message, UtilDate.getFomtTimeByDateString());
		}
		return ConsumeOrderlyStatus.SUCCESS;
	}

}
