package com.nft.admin.collection.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nft.collection.param.PayOrderQueryCondParam;
import com.nft.collection.service.TransactionService;
import com.nft.collection.vo.PayOrderVO;
import com.nft.common.vo.PageResult;
import com.nft.common.vo.Result;

@RestController
@RequestMapping("/payOrder")
public class PayOrderController {

	@Autowired
	private TransactionService transactionService;

	@GetMapping("/findPayOrderByPage")
	public Result<PageResult<PayOrderVO>> findPayOrderByPage(PayOrderQueryCondParam param) {
		return Result.success(transactionService.findPayOrderByPage(param));
	}

}
