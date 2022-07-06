package com.nft.collection.param;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class CancelPayParam {
	
	@NotBlank
	private String memberId;
	
	@NotBlank
	private String orderId;

}
