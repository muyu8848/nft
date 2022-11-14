package com.nft.admin.backgroundaccount.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nft.backgroundaccount.service.RbacService;
import com.nft.backgroundaccount.vo.AccountAuthInfoVO;
import com.nft.common.exception.BizException;
import com.nft.common.vo.Result;
import com.nft.common.vo.TokenInfo;
import com.nft.constants.Constant;
import com.nft.log.domain.LoginLog;
import com.nft.log.service.LoginLogService;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.useragent.UserAgentUtil;

@Controller
public class LoginController {

	@Autowired
	private RbacService rbacService;

	@Autowired
	private LoginLogService loginLogService;

	@PostMapping("/login")
	@ResponseBody
	public Result<TokenInfo> login(String userName, String loginPwd, HttpServletRequest request) {
		LoginLog loginlLog = LoginLog.buildLog(userName, Constant.子系统_后台管理, ServletUtil.getClientIP(request),
				UserAgentUtil.parse(request.getHeader("User-Agent")));
		AccountAuthInfoVO vo = rbacService.getAccountAuthInfo(userName);
		if (vo == null) {
			loginLogService.recordLoginLog(loginlLog.loginFail("账号或密码不正确"));
			throw new BizException("账号或密码不正确");
		}
		if (!SaSecureUtil.sha256(loginPwd).equals(vo.getLoginPwd())) {
			loginLogService.recordLoginLog(loginlLog.loginFail("账号或密码不正确"));
			throw new BizException("账号或密码不正确");
		}
		if (Constant.功能状态_禁用.equals(vo.getState())) {
			loginLogService.recordLoginLog(loginlLog.loginFail("账号已被禁用"));
			throw new BizException("账号已被禁用");
		}
		loginLogService.recordLoginLog(loginlLog.loginSuccess());
		rbacService.updateLatelyLoginTime(vo.getId());
		StpUtil.login(vo.getId(), false);
		StpUtil.getSession().set("userName", vo.getUserName());
		return Result.success(TokenInfo.build());
	}

	@PostMapping("/logout")
	@ResponseBody
	public Result<String> login() {
		StpUtil.logout();
		return Result.success();
	}

}
