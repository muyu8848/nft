package com.nft.setting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nft.common.vo.Result;
import com.nft.setting.service.SettingService;

@Controller
@RequestMapping("/setting")
public class SettingController {

	@Autowired
	private SettingService service;

	@GetMapping("/getCustomerServiceUrl")
	@ResponseBody
	public Result<String> getCustomerServiceUrl() {
		return Result.success(service.getSystemSetting().getCustomerServiceUrl());
	}

}
