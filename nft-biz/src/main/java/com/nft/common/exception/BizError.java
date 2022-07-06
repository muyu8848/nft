package com.nft.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BizError {

	请登录(999, "请登录"),

	参数异常(1000, "参数异常"),

	业务异常(1001, "业务异常");

	private Integer code;

	private String msg;

}
