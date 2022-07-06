package com.nft.common.vo;

import java.io.Serializable;

/**
 * 通用返回结果
 * 
 * @param <T>
 */
public class Result<T> implements Serializable {

	private static final long serialVersionUID = 2L;

	/**
	 * 成功标志 true-响应正常
	 */
	private boolean success;

	/**
	 * 失败消息
	 */
	private String msg;

	/**
	 * 响应代码 200-响应正常
	 */
	private Integer code;

	/**
	 * 时间戳
	 */
	private long timestamp = System.currentTimeMillis();

	/**
	 * 返回的数据
	 */
	private T data;

	public static <T> Result<T> success(T data) {
		Result<T> result = new Result<T>();
		result.setSuccess(true);
		result.setCode(200);
		result.setMsg("success");
		result.setData(data);
		return result;
	}

	public static Result<String> success() {
		Result<String> result = new Result<String>();
		result.setSuccess(true);
		result.setCode(200);
		result.setMsg("success");
		return result;
	}

	/**
	 * 
	 * @param errorMsg
	 * @return
	 */
	public static Result<String> fail(String errorMsg) {
		Result<String> result = new Result<String>();
		result.setSuccess(false);
		result.setCode(500);
		result.setMsg(errorMsg);
		return result;
	}

	public static Result<String> fail(Integer code, String msg) {
		Result<String> result = new Result<String>();
		result.setSuccess(false);
		result.setCode(code);
		result.setMsg(msg);
		return result;
	}

	public boolean isSuccess() {
		return success;
	}

	public Result<T> setSuccess(boolean success) {
		this.success = success;
		return this;
	}

	public String getMsg() {
		return msg;
	}

	public Result<T> setMsg(String msg) {
		this.msg = msg;
		return this;
	}

	public Integer getCode() {
		return code;
	}

	public Result<T> setCode(Integer code) {
		this.code = code;
		return this;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public Result<T> setTimestamp(long timestamp) {
		this.timestamp = timestamp;
		return this;
	}

	public T getData() {
		return data;
	}

	public Result<T> setData(T data) {
		this.data = data;
		return this;
	}

}
