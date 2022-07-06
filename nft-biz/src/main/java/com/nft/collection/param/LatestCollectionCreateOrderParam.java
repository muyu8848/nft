package com.nft.collection.param;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class LatestCollectionCreateOrderParam {

	@NotBlank
	private String memberId;

	@NotBlank
	private String collectionId;

}
