package com.nft.collection.vo;

import java.util.ArrayList;
import java.util.List;

import com.nft.collection.domain.Creator;
import com.nft.collection.domain.IssuedCollection;
import com.nft.collection.domain.MemberResaleCollection;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;

@Data
public class ResaleCollectionVO {

	private String id;

	private String collectionName;

	private String collectionCover;

	private Integer quantity;

	private Integer collectionSerialNumber;

	private Double resalePrice;

	private String creatorName;

	public static List<ResaleCollectionVO> convertFor(List<MemberResaleCollection> pos) {
		if (CollectionUtil.isEmpty(pos)) {
			return new ArrayList<>();
		}
		List<ResaleCollectionVO> vos = new ArrayList<>();
		for (MemberResaleCollection po : pos) {
			vos.add(convertFor(po));
		}
		return vos;
	}

	public static ResaleCollectionVO convertFor(MemberResaleCollection po) {
		if (po == null) {
			return null;
		}
		ResaleCollectionVO vo = new ResaleCollectionVO();
		vo.setId(po.getId());
		vo.setResalePrice(po.getResalePrice());
		if (po.getCollection() != null) {
			vo.setCollectionName(po.getCollection().getName());
			vo.setCollectionCover(po.getCollection().getCover());
			vo.setQuantity(po.getCollection().getQuantity());
			IssuedCollection issuedCollection = po.getIssuedCollection();
			if (issuedCollection != null) {
				vo.setCollectionSerialNumber(issuedCollection.getCollectionSerialNumber());
			}
			Creator creator = po.getCollection().getCreator();
			if (creator != null) {
				vo.setCreatorName(creator.getName());
			}
		}
		return vo;
	}

}
