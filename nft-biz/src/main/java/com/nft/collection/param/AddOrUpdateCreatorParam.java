package com.nft.collection.param;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.BeanUtils;

import com.nft.collection.domain.Creator;
import com.nft.common.utils.IdUtils;

import lombok.Data;

@Data
public class AddOrUpdateCreatorParam {

	private String id;

	@NotBlank
	private String name;

	@NotBlank
	private String avatar;
	
	public Creator convertToPo() {
		Creator po = new Creator();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setDeletedFlag(false);
		po.setCreateTime(new Date());
		po.setLastModifyTime(po.getCreateTime());
		return po;
	}

}
