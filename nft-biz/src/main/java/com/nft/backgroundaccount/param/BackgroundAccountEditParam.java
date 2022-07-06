package com.nft.backgroundaccount.param;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class BackgroundAccountEditParam {

	@NotBlank
	private String id;

	@NotBlank
	private String userName;


	@NotBlank
	private String state;

}
