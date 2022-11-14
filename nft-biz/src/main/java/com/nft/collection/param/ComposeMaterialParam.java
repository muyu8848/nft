package com.nft.collection.param;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ComposeMaterialParam {

	@NotNull
	@DecimalMin(value = "0", inclusive = false)
	private Integer quantity;

	@NotBlank
	private String materialId;

}
