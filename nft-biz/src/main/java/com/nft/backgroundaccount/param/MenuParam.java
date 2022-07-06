package com.nft.backgroundaccount.param;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.BeanUtils;

import com.nft.backgroundaccount.domain.Menu;
import com.nft.common.utils.IdUtils;

import lombok.Data;

@Data
public class MenuParam {

	private String id;

	@NotBlank
	private String name;

	private String url;

	@NotBlank
	private String type;

	private Double orderNo;

	private String parentId;

	public Menu convertToPo() {
		Menu po = new Menu();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setDeletedFlag(false);
		return po;
	}

}
