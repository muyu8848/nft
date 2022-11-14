package com.nft.common.exception;

import java.util.Iterator;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path.Node;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.nft.common.vo.Result;
import com.zengtengpeng.excepiton.LockException;

import cn.dev33.satoken.exception.NotLoginException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler(NotLoginException.class)
	@ResponseStatus(value = HttpStatus.OK)
	public Result<String> handleNotLoginException(NotLoginException e) {
		String msg = "未登录";
		if (e != null) {
			String type = e.getType();
			if (NotLoginException.NOT_TOKEN.equals(type)) {
				msg = "请先登录";
			} else if (NotLoginException.INVALID_TOKEN.equals(type)) {
				msg = "请先登录";
			} else if (NotLoginException.TOKEN_TIMEOUT.equals(type)) {
				msg = "登录已过期";
			} else if (NotLoginException.BE_REPLACED.equals(type)) {
				msg = "账号在另一设备登录，你已被强制下线";
			} else if (NotLoginException.KICK_OUT.equals(type)) {
				msg = "账号在另一设备登录，你已被强制下线";
			}
			log.warn(e.getMessage());
		}
		return Result.fail(BizError.请登录.getCode(), msg);
	}

	@ExceptionHandler(LockException.class)
	@ResponseStatus(value = HttpStatus.OK)
	public Result<String> handleLockException(LockException e) {
		String msg = "lock exception";
		if (e != null) {
			msg = e.getMessage();
			log.warn(e.toString());
		}
		return Result.fail(msg);
	}

	@ExceptionHandler(BizException.class)
	@ResponseStatus(value = HttpStatus.OK)
	public Result<String> handleBizException(BizException e) {
		String msg = "biz exception";
		if (e != null) {
			msg = e.getMsg();
			log.warn(e.toString());
		}
		return Result.fail(BizError.业务异常.getCode(), msg);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(value = HttpStatus.OK)
	public Result<String> handleConstraintViolationException(ConstraintViolationException e) {
		String msg = "param valid exception";
		if (e != null) {
			Iterator<ConstraintViolation<?>> iterator = e.getConstraintViolations().iterator();
			if (iterator.hasNext()) {
				ConstraintViolation<?> violation = iterator.next();
				String paramName = "";
				for (Node node : violation.getPropertyPath()) {
					paramName = node.getName();
				}
				msg = paramName + " " + violation.getMessage();
			}
			log.warn(e.toString());
		}
		return Result.fail(BizError.参数异常.getCode(), msg);
	}

	@ExceptionHandler(value = BindException.class)
	@ResponseStatus(value = HttpStatus.OK)
	public Result<String> handleBindException(BindException e) {
		String msg = "param valid exception";
		if (e != null) {
			Iterator<FieldError> iterator = e.getBindingResult().getFieldErrors().iterator();
			if (iterator.hasNext()) {
				FieldError fieldError = iterator.next();
				String paramName = fieldError.getField();
				msg = paramName + " " + fieldError.getDefaultMessage();
			}
		}
		return Result.fail(BizError.参数异常.getCode(), msg);
	}

}
