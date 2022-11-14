package com.nft.collection.vo.statistic;

import lombok.Data;

@Data
public class TradeStatisticDataVO {

	private Double todayAmount = 0d;

	private Integer todayCount = 0;

	private Double yesterdayAmount = 0d;

	private Double totalAmount = 0d;

}
