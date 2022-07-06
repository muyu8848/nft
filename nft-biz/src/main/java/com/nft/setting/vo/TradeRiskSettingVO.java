package com.nft.setting.vo;

import org.springframework.beans.BeanUtils;

import com.nft.setting.domain.TradeRiskSetting;

import lombok.Data;

@Data
public class TradeRiskSettingVO {

	private Long buyerCancelTrade;

	private Long sellerRejectOrder;

	private Long buyerUnPaid;

	private Long notMySelfPaid;

	private Long paidSellerUnConfirm;

	public static TradeRiskSettingVO convertFor(TradeRiskSetting po) {
		TradeRiskSettingVO vo = new TradeRiskSettingVO();
		if (po != null) {
			BeanUtils.copyProperties(po, vo);
		}
		return vo;
	}

}
