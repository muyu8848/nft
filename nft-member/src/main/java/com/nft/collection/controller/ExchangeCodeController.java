package com.nft.collection.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nft.collection.param.ExchangeParam;
import com.nft.collection.service.ExchangeCodeService;
import com.nft.collection.vo.ExchangeResultVO;
import com.nft.common.vo.Result;

import cn.dev33.satoken.stp.StpUtil;

@RestController
@RequestMapping("/exchangeCode")
public class ExchangeCodeController {

	@Autowired
	private ExchangeCodeService exchangeCodeService;

	@PostMapping("/exchange")
	public Result<ExchangeResultVO> exchange(ExchangeParam param) {
		param.setMemberId(StpUtil.getLoginIdAsString());
		return Result.success(exchangeCodeService.exchange(param));
	}

}
