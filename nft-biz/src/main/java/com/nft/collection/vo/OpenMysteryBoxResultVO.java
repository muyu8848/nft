package com.nft.collection.vo;

import lombok.Data;

@Data
public class OpenMysteryBoxResultVO {

	private String name;

	private String cover;

	public static OpenMysteryBoxResultVO build(String name, String cover) {
		OpenMysteryBoxResultVO vo = new OpenMysteryBoxResultVO();
		vo.setName(name);
		vo.setCover(cover);
		return vo;
	}

}
