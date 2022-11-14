package com.nft.collection.vo;

import java.util.ArrayList;
import java.util.List;

import com.nft.collection.domain.IssuedCollection;
import com.nft.collection.domain.MemberHoldCollection;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;

@Data
public class MyHoldVO {

	private String id;
	
	private String collectionId;

	private String collectionName;

	private String collectionCover;

	private Integer quantity;

	private Integer collectionSerialNumber;

	public static List<MyHoldVO> convertFor(List<MemberHoldCollection> pos) {
		if (CollectionUtil.isEmpty(pos)) {
			return new ArrayList<>();
		}
		List<MyHoldVO> vos = new ArrayList<>();
		for (MemberHoldCollection po : pos) {
			vos.add(convertFor(po));
		}
		return vos;
	}

	public static MyHoldVO convertFor(MemberHoldCollection po) {
		if (po == null) {
			return null;
		}
		MyHoldVO vo = new MyHoldVO();
		vo.setId(po.getId());
		vo.setCollectionId(po.getCollectionId());
		if (po.getCollection() != null) {
			vo.setCollectionName(po.getCollection().getName());
			vo.setCollectionCover(po.getCollection().getCover());
			vo.setQuantity(po.getCollection().getQuantity());
			IssuedCollection issuedCollection = po.getIssuedCollection();
			if (issuedCollection != null) {
				vo.setCollectionSerialNumber(issuedCollection.getCollectionSerialNumber());
			}
		}
		return vo;
	}

}
