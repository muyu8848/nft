package com.nft.member.param;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SettlementAccountQueryCondParam {

	@NotBlank
	private String memberId;

	private Boolean activated;

	private String type;

}
