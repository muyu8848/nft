package com.nft.collection.param;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class UpdateComposeMaterialParam {
	
	@NotBlank
	private String activityId;
	
	@Valid
	@NotEmpty
	private List<ComposeMaterialParam> materials;

}
