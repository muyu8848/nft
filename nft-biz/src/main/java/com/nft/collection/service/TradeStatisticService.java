package com.nft.collection.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.nft.collection.domain.statistic.EverydayTradeData;
import com.nft.collection.param.statistic.TradeDataQueryCondParam;
import com.nft.collection.repo.statistic.EverydayTradeDataRepo;
import com.nft.collection.vo.statistic.EverydayTradeDataVO;
import com.nft.collection.vo.statistic.TradeStatisticDataVO;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;

@Validated
@Service
public class TradeStatisticService {

	@Autowired
	private EverydayTradeDataRepo everydayTradeDataRepo;

	public TradeStatisticDataVO getTradeStatisticData(TradeDataQueryCondParam param) {
		List<EverydayTradeData> result = everydayTradeDataRepo.findAll(param.buildSpecification(),
				Sort.by(Sort.Order.desc("everyday")));
		TradeStatisticDataVO vo = new TradeStatisticDataVO();
		String today = DateUtil.today();
		String yesterday = DateUtil.format(DateUtil.yesterday(), DatePattern.NORM_DATE_PATTERN);
		for (EverydayTradeData data : result) {
			String everyday = DateUtil.format(data.getEveryday(), DatePattern.NORM_DATE_PATTERN);
			if (everyday.equals(today)) {
				vo.setTodayAmount(NumberUtil.round(vo.getTodayAmount() + data.getSuccessAmount(), 2).doubleValue());
				vo.setTodayCount(vo.getTodayCount() + data.getSuccessCount());
			}
			if (everyday.equals(yesterday)) {
				vo.setYesterdayAmount(
						NumberUtil.round(vo.getYesterdayAmount() + data.getSuccessAmount(), 2).doubleValue());
			}
			vo.setTotalAmount(NumberUtil.round(vo.getTotalAmount() + data.getSuccessAmount(), 2).doubleValue());
		}
		return vo;
	}

	@Transactional(readOnly = true)
	public List<EverydayTradeDataVO> findEverydayTradeData(TradeDataQueryCondParam param) {
		List<EverydayTradeData> result = everydayTradeDataRepo.findAll(param.buildSpecification(),
				Sort.by(Sort.Order.asc("everyday")));
		return EverydayTradeDataVO.convertFor(result);
	}

}
