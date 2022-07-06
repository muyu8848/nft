package com.nft.backgroundaccount.param;

import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class AssignRoleParam {

	@NotBlank
	private String accountId;

	private List<String> roleIds;

}
