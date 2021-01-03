package com.lzb.rock.demo.enums;

import com.lzb.rock.base.facade.IMqEnum;

/**
 * 消息枚举
 * 
 * @author lzb
 *
 */
public enum DemoMqEnum implements IMqEnum {

	/**
	 * 顺序消息
	 */
	DEMO_ORDER_BY("DEMO_ORDER_BY", "DEMO_ORDER_BY"),
	
	/**
	 * 普通消息
	 */
	DEMO_TEST("DEMO_TEST", "DEMO_TEST");



	;

	private String topic;

	private String tag;

	DemoMqEnum(String topic, String tag) {
		this.topic = topic;
		this.tag = tag;
	}

	@Override
	public String getTopic() {
		return topic;
	}

	@Override
	public String getTag() {
		return tag;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

}
