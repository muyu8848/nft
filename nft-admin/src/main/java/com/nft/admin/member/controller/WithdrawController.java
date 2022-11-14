package com.nft.admin.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nft.common.operlog.OperLog;
import com.nft.common.vo.PageResult;
import com.nft.common.vo.Result;
import com.nft.constants.Constant;
import com.nft.member.param.WithdrawRecordQueryCondParam;
import com.nft.member.service.WithdrawService;
import com.nft.member.vo.WithdrawRecordVO;

@Controller
@RequestMapping("/withdraw")
public class WithdrawController {

	public static final String 模块_提现 = "提现";

	@Autowired
	private WithdrawService withdrawService;

	@GetMapping("/findByPage")
	@ResponseBody
	public Result<PageResult<WithdrawRecordVO>> findByPage(WithdrawRecordQueryCondParam param) {
		return Result.success(withdrawService.findByPage(param));
	}

	@OperLog(subSystem = Constant.子系统_后台管理, module = 模块_提现, operate = "确定已提现")
	@GetMapping("/confirmCredited")
	@ResponseBody
	public Result<String> confirmCredited(String id) {
		withdrawService.confirmCredited(id);
		return Result.success();
	}

	@OperLog(subSystem = Constant.子系统_后台管理, module = 模块_提现, operate = "驳回")
	@GetMapping("/reject")
	@ResponseBody
	public Result<String> reject(String id) {
		withdrawService.reject(id);
		return Result.success();
	}

}
