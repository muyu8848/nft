package com.nft.collection.vo;

import java.util.ArrayList;
import java.util.List;

import com.nft.collection.domain.Creator;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;

@Data
public class PublishedBrandDictVO {

	private String id;

	private String label;
	
	private List<PublishedCollectionDictVO> collections = new ArrayList<>();

	public static List<PublishedBrandDictVO> convertFor(List<Creator> pos) {
		if (CollectionUtil.isEmpty(pos)) {
			return new ArrayList<>();
		}
		List<PublishedBrandDictVO> vos = new ArrayList<>();
		for (Creator po : pos) {
			vos.add(convertFor(po));
		}
		return vos;
	}

	public static PublishedBrandDictVO convertFor(Creator po) {
		if (po == null) {
			return null;
		}
		PublishedBrandDictVO vo = new PublishedBrandDictVO();
		vo.setId(po.getId());
		vo.setLabel(po.getName());
		return vo;
	}

}
