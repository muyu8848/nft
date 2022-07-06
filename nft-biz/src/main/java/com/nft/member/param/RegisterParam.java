package com.nft.member.param;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class RegisterParam {

	@NotBlank
	private String mobile;
	
	@NotBlank
	private String verificationCode;

	@NotBlank
	private String nickName;

	@NotBlank
	private String loginPwd;

}
