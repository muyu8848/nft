package com.nft.collection.param;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class CollectionCancelResaleParam {
	
	@NotBlank
	private String memberId;

	@NotBlank
	private String holdCollectionId;

}
