package com.nft.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nft.common.vo.PageResult;
import com.nft.common.vo.Result;
import com.nft.log.param.MemberBalanceChangeLogQueryCondParam;
import com.nft.log.service.MemberBalanceChangeLogService;
import com.nft.log.vo.MemberFinanceDetailVO;
import com.nft.log.vo.MemberFinanceRecordVO;

import cn.dev33.satoken.stp.StpUtil;

@RestController
@RequestMapping("/memberBalanceChangeLog")
public class MemberBalanceChangeLogController {

	@Autowired
	private MemberBalanceChangeLogService memberBalanceChangeLogService;

	@GetMapping("/getDetail")
	public Result<MemberFinanceDetailVO> getDetail(String id) {
		return Result.success(memberBalanceChangeLogService.getDetail(id));
	}

	@GetMapping("/findByPage")
	public Result<PageResult<MemberFinanceRecordVO>> findByPage(MemberBalanceChangeLogQueryCondParam param) {
		param.setMemberId(StpUtil.getLoginIdAsString());
		return Result.success(memberBalanceChangeLogService.findMemberFinanceRecordByPage(param));
	}

}
