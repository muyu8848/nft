package com.nft.collection.vo;

import java.util.ArrayList;
import java.util.List;

import com.nft.collection.domain.MemberResaleCollection;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import lombok.Data;

@Data
public class MyResaleCollectionVO {

	private String id;

	private String collectionName;

	private String collectionCover;

	private String resaleDate;
	
	private Double resalePrice;

	public static List<MyResaleCollectionVO> convertFor(List<MemberResaleCollection> pos) {
		if (CollectionUtil.isEmpty(pos)) {
			return new ArrayList<>();
		}
		List<MyResaleCollectionVO> vos = new ArrayList<>();
		for (MemberResaleCollection po : pos) {
			vos.add(convertFor(po));
		}
		return vos;
	}

	public static MyResaleCollectionVO convertFor(MemberResaleCollection po) {
		if (po == null) {
			return null;
		}
		MyResaleCollectionVO vo = new MyResaleCollectionVO();
		vo.setId(po.getId());
		vo.setResaleDate(DateUtil.format(po.getResaleTime(), DatePattern.NORM_DATE_PATTERN));
		vo.setResalePrice(po.getResalePrice());
		if (po.getCollection() != null) {
			vo.setCollectionName(po.getCollection().getName());
			vo.setCollectionCover(po.getCollection().getCover());
		}
		return vo;
	}

}
