package com.nft.dictconfig.param;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.BeanUtils;

import com.nft.common.utils.IdUtils;
import com.nft.dictconfig.domain.DictType;

import lombok.Data;

@Data
public class AddOrUpdateDictTypeParam {

	private String id;

	@NotBlank
	private String dictTypeCode;

	@NotBlank
	private String dictTypeName;

	private String note;
	
	public DictType convertToPo() {
		DictType po = new DictType();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setLastModifyTime(new Date());
		return po;
	}

}
