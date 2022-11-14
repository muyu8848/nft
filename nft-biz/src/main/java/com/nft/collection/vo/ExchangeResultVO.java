package com.nft.collection.vo;

import lombok.Data;

@Data
public class ExchangeResultVO {

	private String name;

	private String cover;

	public static ExchangeResultVO build(String name, String cover) {
		ExchangeResultVO vo = new ExchangeResultVO();
		vo.setName(name);
		vo.setCover(cover);
		return vo;
	}

}
