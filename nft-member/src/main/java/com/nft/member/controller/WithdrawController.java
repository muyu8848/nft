package com.nft.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nft.common.vo.PageResult;
import com.nft.common.vo.Result;
import com.nft.member.param.WithdrawParam;
import com.nft.member.param.WithdrawRecordQueryCondParam;
import com.nft.member.service.WithdrawService;
import com.nft.member.vo.WithdrawRecordVO;

import cn.dev33.satoken.stp.StpUtil;

@Controller
@RequestMapping("/withdraw")
public class WithdrawController {

	@Autowired
	private WithdrawService withdrawService;

	@PostMapping("/withdraw")
	@ResponseBody
	public Result<String> withdraw(WithdrawParam param) {
		param.setMemberId(StpUtil.getLoginIdAsString());
		withdrawService.withdraw(param);
		return Result.success();
	}

	@GetMapping("/findByPage")
	@ResponseBody
	public Result<PageResult<WithdrawRecordVO>> findByPage(WithdrawRecordQueryCondParam param) {
		param.setMemberId(StpUtil.getLoginIdAsString());
		return Result.success(withdrawService.findByPage(param));
	}

}
