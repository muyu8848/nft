package com.nft.collection.param;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.BeanUtils;

import com.nft.collection.domain.PreSaleCondition;
import com.nft.common.utils.IdUtils;

import lombok.Data;

@Data
public class PreSaleConditionParam {

	@NotBlank
	private String fieldName;

	@NotBlank
	private String cond;

	@NotBlank
	private String fieldValue;

	private String logicalOperation;

	public PreSaleCondition convertToPo(String preSaleTaskId, Double orderNo) {
		PreSaleCondition po = new PreSaleCondition();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setPreSaleTaskId(preSaleTaskId);
		po.setOrderNo(orderNo);
		return po;
	}

}
