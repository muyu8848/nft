package com.nft.member.param;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class BindRealNameParam {

	@NotBlank
	private String realName;

	@NotBlank
	private String identityCard;

	@NotBlank
	private String memberId;

}
