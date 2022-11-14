package com.nft.admin.dataclean.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nft.common.operlog.OperLog;
import com.nft.common.vo.Result;
import com.nft.constants.Constant;
import com.nft.dataclean.param.DataCleanParam;
import com.nft.dataclean.service.DataCleanService;

@Controller
@RequestMapping("/dataClean")
public class DataCleanController {

	@Autowired
	private DataCleanService dataCleanService;

	@OperLog(subSystem = Constant.子系统_后台管理, module = "数据清理", operate = "数据清理")
	@PostMapping("/clean")
	@ResponseBody
	public Result<String> clean(@RequestBody DataCleanParam param) {
		dataCleanService.clean(param);
		return Result.success();
	}

}
