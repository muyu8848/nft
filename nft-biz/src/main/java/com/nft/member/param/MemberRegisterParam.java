package com.nft.member.param;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class MemberRegisterParam {

	private String inviteCode;

	@NotBlank
	@Pattern(regexp = "^[A-Za-z][A-Za-z0-9]{2,11}$")
	private String userName;

	@NotBlank
	@Pattern(regexp = "^[A-Za-z][A-Za-z0-9]{5,14}$")
	private String loginPwd;

}
