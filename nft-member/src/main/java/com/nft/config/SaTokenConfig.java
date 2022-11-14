package com.nft.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.nft.common.exception.BizException;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.fun.SaParamFunction;
import cn.dev33.satoken.interceptor.SaRouteInterceptor;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.router.SaRouterStaff;
import cn.dev33.satoken.stp.StpUtil;

@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new SaRouteInterceptor((req, res, handler) -> {
			SaRouter.match("/**", new SaParamFunction<SaRouterStaff>() {

				@Override
				public void run(SaRouterStaff r) {
					StpUtil.checkLogin();
					StpUtil.hasRole("member");
				}
			});
		})).addPathPatterns("/**")
				.excludePathPatterns("/login", "/sendLoginVerificationCode", "/dictconfig/**", "/setting/**",
						"/storage/fetch/**", "/storage/inviteQrcode/**", "/error")
				.excludePathPatterns("/notice/**", "/collection/**").excludePathPatterns("/favicon.ico");
	}

	@Bean
	public SaServletFilter getSaServletFilter() {
		return new SaServletFilter().addInclude("/**").addExclude("/favicon.ico")

				// 全局认证函数
				.setAuth(obj -> {
				})

				// 异常处理函数
				.setError(e -> {
					throw new BizException(e.getMessage());
				})

				// 前置函数：在每次认证函数之前执行
				.setBeforeAuth(obj -> {
					// ---------- 设置跨域响应头 ----------
					SaHolder.getResponse()
							// 允许指定域访问跨域资源
							.setHeader("Access-Control-Allow-Origin", "*")
							// 允许所有请求方式
							.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE")
							// 有效时间
							.setHeader("Access-Control-Max-Age", "3600")
							// 允许的header参数
							.setHeader("Access-Control-Allow-Headers", "*");
					// 如果是预检请求，则立即返回到前端
					SaRouter.match(SaHttpMethod.OPTIONS).free(r -> {
					}).back();
				});
	}

}
