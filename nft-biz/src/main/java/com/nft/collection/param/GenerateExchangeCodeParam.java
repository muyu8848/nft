package com.nft.collection.param;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class GenerateExchangeCodeParam {

	@NotBlank
	private String collectionId;

	@NotNull
	@DecimalMin(value = "0", inclusive = false)
	@DecimalMax(value = "2000", inclusive = false)
	private Integer quantity;

}
