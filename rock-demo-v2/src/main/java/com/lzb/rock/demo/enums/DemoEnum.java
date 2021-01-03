package com.lzb.rock.demo.enums;

import com.google.common.collect.ImmutableMap;
import com.lzb.rock.base.facade.IEnum;

/**
 * 消息枚举
 * 
 * @author lzb
 *
 */
public enum DemoEnum implements IEnum {

	// 系统级别错误码,系统级别错误码小于2000
	USER_ACCOUNT_ERR("3001", "用户名不存在"),

	;

	private String code;
	private String msg;

	DemoEnum(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	/**
	 * 枚举类型的判断和获取
	 * 
	 * @param code 错误码
	 * @return 返回错误码对应的枚举信息
	 */
	public static DemoEnum statusOf(String code) {
		for (DemoEnum demoEnum : values()) {
			if (demoEnum.getCode().equals(code)) {
				return demoEnum;
			}
		}
		return null;
	}

	@Override
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg == null ? "" : msg.trim();
	}

	/**
	 * 枚举转换为MAP
	 * 
	 * @return
	 */
	public ImmutableMap<String, String> toMap() {
		return ImmutableMap.<String, String>builder().put("msg", msg).put("code", code).build();
	}
}
