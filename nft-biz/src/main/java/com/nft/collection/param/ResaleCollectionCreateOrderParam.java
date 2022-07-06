package com.nft.collection.param;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ResaleCollectionCreateOrderParam {

	@NotBlank
	private String memberId;

	@NotBlank
	private String resaleCollectionId;

}
