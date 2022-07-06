package com.nft.setting.param;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class TradeRiskSettingParam {

	@NotNull
	@DecimalMin(value = "0", inclusive = false)
	private Long buyerCancelTrade;

	@NotNull
	@DecimalMin(value = "0", inclusive = false)
	private Long sellerRejectOrder;

	@NotNull
	@DecimalMin(value = "0", inclusive = false)
	private Long buyerUnPaid;

	@NotNull
	@DecimalMin(value = "0", inclusive = false)
	private Long notMySelfPaid;

	@NotNull
	@DecimalMin(value = "0", inclusive = false)
	private Long paidSellerUnConfirm;

}
