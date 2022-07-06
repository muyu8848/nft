package com.nft.backgroundaccount.param;

import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class AssignMenuParam {
	
	@NotBlank
	private String roleId;
	
	private List<String> menuIds;

}
