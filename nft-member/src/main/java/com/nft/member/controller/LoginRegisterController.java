package com.nft.member.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nft.common.exception.BizException;
import com.nft.common.vo.Result;
import com.nft.common.vo.TokenInfo;
import com.nft.constants.Constant;
import com.nft.log.domain.LoginLog;
import com.nft.log.service.LoginLogService;
import com.nft.member.param.LoginParam;
import com.nft.member.service.MemberService;
import com.nft.member.vo.AccountAuthInfoVO;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.useragent.UserAgentUtil;

@RestController
public class LoginRegisterController {

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	private MemberService memberService;

	@Autowired
	private LoginLogService loginLogService;

	@GetMapping("/sendLoginVerificationCode")
	public Result<String> sendLoginVerificationCode(String mobile) {
		memberService.sendLoginVerificationCode(mobile);
		return Result.success();
	}

	@PostMapping("/login")
	public Result<TokenInfo> login(LoginParam param, HttpServletRequest request) {
		LoginLog loginlLog = LoginLog.buildLog(param.getMobile(), Constant.子系统_会员端, ServletUtil.getClientIP(request),
				UserAgentUtil.parse(request.getHeader("User-Agent")));
		String verificationCode = redisTemplate.opsForValue().get(Constant.短信类型_验证码_登录 + param.getMobile());
		if (!param.getVerificationCode().equals(verificationCode)) {
			loginLogService.recordLoginLog(loginlLog.loginFail("验证码不正确"));
			throw new BizException("验证码不正确");
		}
		AccountAuthInfoVO vo = memberService.getAccountAuthInfo(param.getMobile());
		if (vo == null) {
			memberService.registerAccount(param.getMobile(), param.getInviteCode());
			vo = memberService.getAccountAuthInfo(param.getMobile());
		}
		if (Constant.功能状态_禁用.equals(vo.getState())) {
			loginLogService.recordLoginLog(loginlLog.loginFail("账号已被禁用"));
			throw new BizException("账号已被禁用");
		}
		loginLogService.recordLoginLog(loginlLog.loginSuccess());
		memberService.updateLatelyLoginTime(vo.getId());
		// StpUtil.kickout(vo.getId());
		StpUtil.login(vo.getId(),
				new SaLoginModel().setIsLastingCookie(false).setTimeout(60 * 60 * vo.getKeepLoginDuration()));
		StpUtil.getSession().set("mobile", vo.getMobile());
		TokenInfo tokenInfo = TokenInfo.build();
		tokenInfo.setAccountId(vo.getId());
		return Result.success(tokenInfo);
	}

	@PostMapping("/logout")
	public Result<String> login() {
		StpUtil.logout();
		return Result.success();
	}

}
