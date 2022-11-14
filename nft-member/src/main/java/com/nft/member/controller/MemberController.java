package com.nft.member.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nft.common.vo.PageResult;
import com.nft.common.vo.Result;
import com.nft.constants.Constant;
import com.nft.log.param.LoginLogQueryCondParam;
import com.nft.log.service.LoginLogService;
import com.nft.log.vo.LoginLogVO;
import com.nft.member.param.BindRealNameParam;
import com.nft.member.param.ModifyPayPwdParam;
import com.nft.member.service.MemberService;
import com.nft.member.vo.InviteInfoVO;
import com.nft.member.vo.MyInviteRecordVO;
import com.nft.member.vo.MyPersonalInfoVO;

import cn.dev33.satoken.stp.StpUtil;

@RestController
@RequestMapping("/member")
public class MemberController {

	@Autowired
	private MemberService memberService;

	@Autowired
	private LoginLogService loginLogService;

	@GetMapping("/findMyInviteRecord")
	public Result<List<MyInviteRecordVO>> findMyInviteRecord() {
		return Result.success(memberService.findMyInviteRecord(StpUtil.getLoginIdAsString()));
	}

	@GetMapping("/getInviteInfo")
	public Result<InviteInfoVO> getInviteInfo() {
		return Result.success(memberService.getInviteInfo(StpUtil.getLoginIdAsString()));
	}

	@GetMapping("/getBalance")
	public Result<Double> getBalance() {
		return Result.success(memberService.getBalance(StpUtil.getLoginIdAsString()));
	}

	@GetMapping("/sendModifyPayPwdVerificationCode")
	public Result<String> sendModifyPayPwdVerificationCode() {
		memberService.sendModifyPayPwdVerificationCode(StpUtil.getLoginIdAsString());
		return Result.success();
	}

	@GetMapping("/findLoginLogByPage")
	public Result<PageResult<LoginLogVO>> findLoginLogByPage(LoginLogQueryCondParam param) {
		param.setUserName(StpUtil.getSession().getString("mobile"));
		param.setSubSystem(Constant.子系统_会员端);
		return Result.success(loginLogService.findLoginLogByPage(param));
	}

	@PostMapping("/updateKeepLoginDuration")
	public Result<String> updateKeepLoginDuration(Integer keepLoginDuration) {
		memberService.updateKeepLoginDuration(StpUtil.getLoginIdAsString(), keepLoginDuration);
		return Result.success();
	}

	@GetMapping("/getMyPersonalInfo")
	public Result<MyPersonalInfoVO> getMyPersonalInfo() {
		return Result.success(memberService.getMyPersonalInfo(StpUtil.getLoginIdAsString()));
	}

	@PostMapping("/updateNickName")
	public Result<String> updateNickName(String nickName) {
		memberService.updateNickName(StpUtil.getLoginIdAsString(), nickName);
		return Result.success();
	}

	@PostMapping("/updateAvatar")
	public Result<String> updateAvatar(String avatar) {
		memberService.updateAvatar(StpUtil.getLoginIdAsString(), avatar);
		return Result.success();
	}

	@PostMapping("/bindRealName")
	public Result<String> bindRealName(BindRealNameParam param) {
		param.setMemberId(StpUtil.getLoginIdAsString());
		memberService.bindRealName(param);
		return Result.success();
	}

	@PostMapping("/modifyPayPwd")
	public Result<String> modifyPayPwd(ModifyPayPwdParam param) {
		param.setMemberId(StpUtil.getLoginIdAsString());
		memberService.modifyPayPwd(param);
		return Result.success();
	}

}
