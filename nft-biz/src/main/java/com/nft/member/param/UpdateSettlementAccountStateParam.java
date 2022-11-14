package com.nft.member.param;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UpdateSettlementAccountStateParam {

	@NotBlank
	private String id;

	@NotNull
	private Boolean activated;

	@NotBlank
	private String memberId;

}
