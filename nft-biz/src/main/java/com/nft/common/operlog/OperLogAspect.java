package com.nft.common.operlog;

import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.nft.common.utils.IdUtils;
import com.nft.log.service.OperLogService;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.HttpUtil;

@Aspect
@Component
public class OperLogAspect {

	@Autowired
	private OperLogService operLogService;

	@Pointcut("@annotation(com.nft.common.operlog.OperLog)")
	public void operLogAspect() {
	}

	@AfterReturning(pointcut = "operLogAspect()", returning = "result")
	public void doAfterReturning(JoinPoint joinPoint, Object result) {
		recordOperLog(joinPoint, null, result);
	}

	@AfterThrowing(value = "operLogAspect()", throwing = "e")
	public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
		recordOperLog(joinPoint, e, null);
	}

	public void recordOperLog(JoinPoint joinPoint, Exception e, Object result) {
		OperLog annotation = getOperLogAnnotation(joinPoint);
		if (annotation == null) {
			return;
		}
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		HttpServletRequest request = requestAttributes.getRequest();
		String operAccountId = StpUtil.getLoginIdAsString();
		String operName = StpUtil.getSession().getString("userName");

		com.nft.log.domain.OperLog operLog = new com.nft.log.domain.OperLog();
		operLog.setId(IdUtils.getId());
		operLog.setSubSystem(annotation.subSystem());
		operLog.setModule(annotation.module());
		operLog.setOperate(annotation.operate());
		operLog.setRequestMethod(request.getMethod());
		operLog.setRequestUrl(request.getRequestURL().toString());
		operLog.setRequestParam(ServletUtil.isMultipart(request) ? "" : JSON.toJSONString(joinPoint.getArgs()));
		operLog.setIpAddr(HttpUtil.getClientIP(request));
		operLog.setOperAccountId(operAccountId);
		operLog.setOperName(operName);
		operLog.setOperTime(new Date());
		operLogService.recordOperLog(operLog);
	}

	private OperLog getOperLogAnnotation(JoinPoint joinPoint) {
		Signature signature = joinPoint.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		Method method = methodSignature.getMethod();
		if (method != null) {
			return method.getAnnotation(OperLog.class);
		}
		return null;
	}

}
