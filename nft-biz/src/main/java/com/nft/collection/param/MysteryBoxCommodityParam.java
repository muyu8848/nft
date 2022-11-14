package com.nft.collection.param;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class MysteryBoxCommodityParam {

	@NotBlank
	private String commodityId;

	@NotNull
	@DecimalMin(value = "0", inclusive = false)
	private Double probability;

}
