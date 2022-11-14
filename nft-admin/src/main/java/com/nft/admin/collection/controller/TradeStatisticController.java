package com.nft.admin.collection.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nft.collection.param.statistic.TradeDataQueryCondParam;
import com.nft.collection.service.TradeStatisticService;
import com.nft.collection.vo.statistic.EverydayTradeDataVO;
import com.nft.collection.vo.statistic.TradeStatisticDataVO;
import com.nft.common.vo.Result;

@RestController
@RequestMapping("/tradeStatistic")
public class TradeStatisticController {

	@Autowired
	private TradeStatisticService tradeStatisticService;

	@GetMapping("/getTradeStatisticData")
	public Result<TradeStatisticDataVO> getTradeStatisticData(TradeDataQueryCondParam param) {
		return Result.success(tradeStatisticService.getTradeStatisticData(param));
	}

	@GetMapping("/findEverydayTradeData")
	public Result<List<EverydayTradeDataVO>> findEverydayTradeData(TradeDataQueryCondParam param) {
		return Result.success(tradeStatisticService.findEverydayTradeData(param));
	}

}
