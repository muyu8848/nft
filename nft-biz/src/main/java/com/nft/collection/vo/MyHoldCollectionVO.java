package com.nft.collection.vo;

import java.util.ArrayList;
import java.util.List;

import com.nft.collection.domain.MemberHoldCollection;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import lombok.Data;

@Data
public class MyHoldCollectionVO {

	private String id;

	private String collectionName;

	private String collectionCover;

	private String holdDate;

	public static List<MyHoldCollectionVO> convertFor(List<MemberHoldCollection> pos) {
		if (CollectionUtil.isEmpty(pos)) {
			return new ArrayList<>();
		}
		List<MyHoldCollectionVO> vos = new ArrayList<>();
		for (MemberHoldCollection po : pos) {
			vos.add(convertFor(po));
		}
		return vos;
	}

	public static MyHoldCollectionVO convertFor(MemberHoldCollection po) {
		if (po == null) {
			return null;
		}
		MyHoldCollectionVO vo = new MyHoldCollectionVO();
		vo.setId(po.getId());
		vo.setHoldDate(DateUtil.format(po.getHoldTime(), DatePattern.NORM_DATE_PATTERN));
		if (po.getCollection() != null) {
			vo.setCollectionName(po.getCollection().getName());
			vo.setCollectionCover(po.getCollection().getCover());
		}
		return vo;
	}

}
