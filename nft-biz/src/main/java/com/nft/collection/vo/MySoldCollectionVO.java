package com.nft.collection.vo;

import java.util.ArrayList;
import java.util.List;

import com.nft.collection.domain.MemberHoldCollection;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import lombok.Data;

@Data
public class MySoldCollectionVO {

	private String id;

	private String collectionName;

	private String collectionCover;

	private String soldDate;

	private Double soldPrice;

	public static List<MySoldCollectionVO> convertFor(List<MemberHoldCollection> pos) {
		if (CollectionUtil.isEmpty(pos)) {
			return new ArrayList<>();
		}
		List<MySoldCollectionVO> vos = new ArrayList<>();
		for (MemberHoldCollection po : pos) {
			vos.add(convertFor(po));
		}
		return vos;
	}

	public static MySoldCollectionVO convertFor(MemberHoldCollection po) {
		if (po == null) {
			return null;
		}
		MySoldCollectionVO vo = new MySoldCollectionVO();
		vo.setId(po.getId());
		vo.setSoldDate(DateUtil.format(po.getLoseTime(), DatePattern.NORM_DATE_PATTERN));
		vo.setSoldPrice(po.getResalePrice());
		if (po.getCollection() != null) {
			vo.setCollectionName(po.getCollection().getName());
			vo.setCollectionCover(po.getCollection().getCover());
		}
		return vo;
	}

}
