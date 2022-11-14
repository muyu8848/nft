package com.nft.collection.vo;

import com.nft.collection.domain.Collection;

import lombok.Data;

@Data
public class PublishedCollectionDictVO {

	private String id;

	private String label;

	private String type;

	public static PublishedCollectionDictVO convertFor(Collection po) {
		if (po == null) {
			return null;
		}
		PublishedCollectionDictVO vo = new PublishedCollectionDictVO();
		vo.setId(po.getId());
		vo.setLabel(po.getName());
		vo.setType(po.getCommodityType());
		return vo;
	}

}
