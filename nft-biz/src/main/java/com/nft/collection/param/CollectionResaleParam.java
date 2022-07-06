package com.nft.collection.param;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CollectionResaleParam {

	@NotBlank
	private String memberId;

	@NotBlank
	private String holdCollectionId;

	@NotNull
	@DecimalMin(value = "0", inclusive = false)
	private Double resalePrice;

}
