package com.nft.collection.param;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class UpdateCollectionStoryParam {

	@NotBlank
	private String collectionId;

	@NotEmpty
	private List<String> picLinks;

}
