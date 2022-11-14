package com.nft.member.param;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.BeanUtils;

import com.nft.common.utils.IdUtils;
import com.nft.member.domain.SettlementAccount;

import lombok.Data;

@Data
public class AddSettlementAccountParam {

	@NotBlank
	private String memberId;

	@NotBlank
	private String type;

	private String cardNumber;

	private String bankName;

	private String account;

	public SettlementAccount convertToPo() {
		SettlementAccount po = new SettlementAccount();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setCreateTime(new Date());
		po.setActivated(false);
		po.setDeletedFlag(false);
		return po;
	}

}
