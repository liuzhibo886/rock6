package com.lzb.rock.base.exception;

import com.lzb.rock.base.facade.IEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 封装rock的异常
 *
 * @author lzb
 * @Date 2017/12/28 下午10:32
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BusClientException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String code;

	private String message;

	String data;

	public BusClientException(IEnum busEnum) {
		this.code = busEnum.getCode();
		this.message = busEnum.getMsg();
	}

	public BusClientException(IEnum busEnum, String message) {
		this.code = busEnum.getCode();
		this.message = message;
	}

	public BusClientException(IEnum busEnum, String message, String data) {
		this.code = busEnum.getCode();
		this.message = message;
		this.data = data;
	}

}
