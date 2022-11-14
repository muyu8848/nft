package com.nft.collection.param;

import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import com.nft.collection.domain.ComposeActivity;
import com.nft.common.utils.IdUtils;

import lombok.Data;

@Data
public class AddComposeActivityParam {

	@NotBlank
	private String title;

	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date activityTimeStart;

	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date activityTimeEnd;

	@NotBlank
	private String collectionId;

	@NotNull
	@DecimalMin(value = "0", inclusive = false)
	private Integer quantity;

	public ComposeActivity convertToPo() {
		ComposeActivity po = new ComposeActivity();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setStock(po.getQuantity());
		po.setDeletedFlag(false);
		po.setCreateTime(new Date());
		return po;
	}

}
