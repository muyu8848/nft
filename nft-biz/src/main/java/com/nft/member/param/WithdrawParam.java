package com.nft.member.param;

import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;

import com.nft.common.utils.IdUtils;
import com.nft.constants.Constant;
import com.nft.member.domain.WithdrawRecord;
import com.nft.member.domain.SettlementAccount;

import cn.hutool.core.util.NumberUtil;
import lombok.Data;

@Data
public class WithdrawParam {

	@NotNull
	@DecimalMin(value = "0", inclusive = false)
	private Double amount;

	@NotBlank
	private String settlementAccountId;

	@NotBlank
	private String memberId;

	public WithdrawRecord convertToPo(SettlementAccount settlementAccount, Double handlingFee) {
		WithdrawRecord po = new WithdrawRecord();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setOrderNo(po.getId());
		po.setSubmitTime(new Date());
		po.setState(Constant.提现记录状态_审核中);
		po.setHandlingFee(handlingFee);
		po.setToTheAccount(NumberUtil.round(po.getAmount() - handlingFee, 2).doubleValue());
		return po;
	}

}
