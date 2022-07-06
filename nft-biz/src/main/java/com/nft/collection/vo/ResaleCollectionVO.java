package com.nft.collection.vo;

import java.util.ArrayList;
import java.util.List;

import com.nft.collection.domain.Creator;
import com.nft.collection.domain.MemberHoldCollection;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;

@Data
public class ResaleCollectionVO {

	private String id;

	private String collectionName;

	private String collectionCover;

	private Double resalePrice;

	private String creatorName;

	public static List<ResaleCollectionVO> convertFor(List<MemberHoldCollection> pos) {
		if (CollectionUtil.isEmpty(pos)) {
			return new ArrayList<>();
		}
		List<ResaleCollectionVO> vos = new ArrayList<>();
		for (MemberHoldCollection po : pos) {
			vos.add(convertFor(po));
		}
		return vos;
	}

	public static ResaleCollectionVO convertFor(MemberHoldCollection po) {
		if (po == null) {
			return null;
		}
		ResaleCollectionVO vo = new ResaleCollectionVO();
		vo.setId(po.getId());
		vo.setResalePrice(po.getResalePrice());
		if (po.getCollection() != null) {
			vo.setCollectionName(po.getCollection().getName());
			vo.setCollectionCover(po.getCollection().getCover());
			Creator creator = po.getCollection().getCreator();
			if (creator != null) {
				vo.setCreatorName(creator.getName());
			}
		}
		return vo;
	}

}
