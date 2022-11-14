package com.nft.collection.param;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class ComposeParam {
	
	@NotBlank
	private String memberId;
	
	@NotBlank
	private String activityId;
	
	@NotEmpty
	private List<String> holdCollectionIds;

}
