package com.nft.common.exception;

import lombok.Getter;

@Getter
public class BizException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer code;

	private String msg;

	public BizException(BizError bizError) {
		super(bizError.getMsg());
		this.code = bizError.getCode();
		this.msg = bizError.getMsg();
	}
	
	public BizException(String msg) {
		super(msg);
		this.code = BizError.业务异常.getCode();
		this.msg = msg;
	}

	public BizException(Integer code, String msg) {
		super(msg);
		this.code = code;
		this.msg = msg;
	}

}
