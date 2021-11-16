package com.lzb.rock.netty.enums;

import com.lzb.rock.base.facade.IEnum;

/**
 * 错误消息枚举
 * @author liuzhibo
 *
 */
public enum NettyEnum implements IEnum {

	CTX_NOT("CTX_NOT", "用户未绑定"),


	;

	private String code;
	private String msg;

	NettyEnum(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param text the text to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

}
