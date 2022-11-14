package com.nft.admin.log.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nft.common.vo.PageResult;
import com.nft.common.vo.Result;
import com.nft.log.param.LoginLogQueryCondParam;
import com.nft.log.service.LoginLogService;
import com.nft.log.vo.LoginLogVO;

@Controller
@RequestMapping("/loginLog")
public class LoginLogController {

	@Autowired
	private LoginLogService loginLogService;

	@GetMapping("/findLoginLogByPage")
	@ResponseBody
	public Result<PageResult<LoginLogVO>> findLoginLogByPage(LoginLogQueryCondParam param) {
		return Result.success(loginLogService.findLoginLogByPage(param));
	}

}
