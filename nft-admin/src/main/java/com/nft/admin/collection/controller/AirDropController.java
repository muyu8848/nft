package com.nft.admin.collection.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nft.collection.param.AirDropParam;
import com.nft.collection.service.AirDropService;
import com.nft.common.operlog.OperLog;
import com.nft.common.vo.Result;
import com.nft.constants.Constant;

@Controller
@RequestMapping("/airDrop")
public class AirDropController {

	public static final String 模块_空投 = "空投";

	@Autowired
	private AirDropService airDropService;

	@OperLog(subSystem = Constant.子系统_后台管理, module = 模块_空投, operate = "发放空投")
	@PostMapping("/airDrop")
	@ResponseBody
	public Result<String> airDrop(AirDropParam param) {
		airDropService.airDrop(param);
		return Result.success();
	}

}
