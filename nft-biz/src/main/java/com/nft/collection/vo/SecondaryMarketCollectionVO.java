package com.nft.collection.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nft.collection.domain.MemberHoldCollection;
import com.nft.dictconfig.DictHolder;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;

@Data
public class SecondaryMarketCollectionVO {

	private String id;

	private String collectionName;

	private String collectionCover;

	private Double resalePrice;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date resaleTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date holdTime;

	private String gainWayName;

	private String memberMobile;

	private String memberBlockChainAddr;

	public static List<SecondaryMarketCollectionVO> convertFor(List<MemberHoldCollection> pos) {
		if (CollectionUtil.isEmpty(pos)) {
			return new ArrayList<>();
		}
		List<SecondaryMarketCollectionVO> vos = new ArrayList<>();
		for (MemberHoldCollection po : pos) {
			vos.add(convertFor(po));
		}
		return vos;
	}

	public static SecondaryMarketCollectionVO convertFor(MemberHoldCollection po) {
		if (po == null) {
			return null;
		}
		SecondaryMarketCollectionVO vo = new SecondaryMarketCollectionVO();
		BeanUtils.copyProperties(po, vo);
		vo.setGainWayName(DictHolder.getDictItemName("collectionGainWay", po.getGainWay()));
		if (po.getCollection() != null) {
			vo.setCollectionName(po.getCollection().getName());
			vo.setCollectionCover(po.getCollection().getCover());
		}
		if (po.getMember() != null) {
			vo.setMemberMobile(po.getMember().getMobile());
			vo.setMemberBlockChainAddr(po.getMember().getBlockChainAddr());
		}
		return vo;
	}

}
