package com.nft.collection.param;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class UpdateMysteryBoxCommodityParam {

	@NotBlank
	private String collectionId;

	@NotEmpty
	private List<MysteryBoxCommodityParam> subCommoditys;

}
