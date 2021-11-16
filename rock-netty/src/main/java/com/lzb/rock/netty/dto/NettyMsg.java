package com.lzb.rock.netty.dto;

import javax.validation.constraints.NotBlank;

import com.alibaba.fastjson.annotation.JSONField;
import com.lzb.rock.base.aop.annotation.RelEnum;
import com.lzb.rock.netty.enums.EventEnum;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息发送对象
 * 
 * @author lzb
 * @Date 2019年9月30日 下午6:00:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NettyMsg {

	public NettyMsg(String event, String body, String sendAccount, String receiveAccount) {
		super();
		this.event = event;
		this.body = body;
		this.sendAccount = sendAccount;
		this.receiveAccount = receiveAccount;
	}

	@ApiModelProperty(value = "消息ID", hidden = true)
	String msgId;

	@ApiModelProperty(value = "事件")
	@RelEnum(EventEnum.class)
	String event;

	@ApiModelProperty(value = "消息内容")
	String body;

	@ApiModelProperty(value = "发送者账号")
	@NotBlank(message = "发送者账号不能为空")
	String sendAccount;

	@ApiModelProperty(value = "接收消息账号")
	@NotBlank(message = "接收者账号不能为空")
	String receiveAccount;

	@ApiModelProperty(value = "最大发送次数")
	@JSONField(serialize = false)
	Integer count = 3;

	@ApiModelProperty(value = "重试时间间隔,毫秒")
	@JSONField(serialize = false)
	Long dueTime = 3000L;
}
