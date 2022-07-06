package com.nft.collection.param;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ConfirmPayParam {
	
	@NotBlank
	private String memberId;
	
	@NotBlank
	private String orderId;

}
