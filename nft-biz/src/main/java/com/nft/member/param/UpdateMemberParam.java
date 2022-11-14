package com.nft.member.param;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class UpdateMemberParam {

	@NotBlank
	private String id;

	@NotBlank
	private String nickName;
	
	@NotBlank
	private String mobile;

	@NotBlank
	private String state;

}
