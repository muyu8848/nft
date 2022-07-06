package com.nft.collection.param;

import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import com.nft.collection.domain.Collection;
import com.nft.common.utils.IdUtils;

import lombok.Data;

@Data
public class AddCollectionParam {

	@NotBlank
	private String name;
	
	@NotBlank
	private String cover;

	@NotNull
	@DecimalMin(value = "0", inclusive = false)
	private Double price;

	@NotNull
	@DecimalMin(value = "0", inclusive = false)
	private Integer quantity;

	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date saleTime;
	
	@NotBlank
	private String creatorId;

	public Collection convertToPo() {
		Collection po = new Collection();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setDeletedFlag(false);
		po.setCreateTime(new Date());
		po.setStock(po.getQuantity());
		return po;
	}

}
