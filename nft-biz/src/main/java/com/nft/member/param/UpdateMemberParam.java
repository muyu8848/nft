package com.nft.member.param;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class UpdateMemberParam {

	@NotBlank
	private String id;

	@NotBlank
	private String nickName;
	
	private String realName;

	@NotBlank
	private String mobile;

	@NotBlank
	private String state;
	
	@NotBlank
	private String buyState;
	
	@NotBlank
	private String sellState;

}
