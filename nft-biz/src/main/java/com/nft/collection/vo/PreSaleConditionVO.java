package com.nft.collection.vo;

import org.springframework.beans.BeanUtils;

import com.nft.collection.domain.PreSaleCondition;
import com.nft.constants.Constant;
import com.nft.dictconfig.DictHolder;

import lombok.Data;

@Data
public class PreSaleConditionVO {

	private String fieldName;

	private String fieldLabel;

	private String cond;

	private String fieldValue;

	private String logicalOperation;

	public static PreSaleConditionVO convertFor(PreSaleCondition po) {
		if (po == null) {
			return null;
		}
		PreSaleConditionVO vo = new PreSaleConditionVO();
		BeanUtils.copyProperties(po, vo);
		vo.setFieldLabel(DictHolder.getDictItemName("snapshotParam", vo.getFieldName()));
		return vo;
	}

}
