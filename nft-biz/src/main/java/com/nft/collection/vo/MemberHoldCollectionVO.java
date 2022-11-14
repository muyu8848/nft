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
public class MemberHoldCollectionVO {

	private String id;

	private String issuedCollectionId;

	private Double price;

	private String gainWay;

	private String gainWayName;

	private String state;

	private String stateName;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date holdTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date loseTime;

	private String uniqueId;

	private String transactionHash;

	private String collectionName;

	private String collectionCover;

	private String memberMobile;

	private String memberBlockChainAddr;

	private Integer collectionSerialNumber;

	private Integer quantity;

	public static List<MemberHoldCollectionVO> convertFor(List<MemberHoldCollection> pos) {
		if (CollectionUtil.isEmpty(pos)) {
			return new ArrayList<>();
		}
		List<MemberHoldCollectionVO> vos = new ArrayList<>();
		for (MemberHoldCollection po : pos) {
			vos.add(convertFor(po));
		}
		return vos;
	}

	public static MemberHoldCollectionVO convertFor(MemberHoldCollection po) {
		if (po == null) {
			return null;
		}
		MemberHoldCollectionVO vo = new MemberHoldCollectionVO();
		BeanUtils.copyProperties(po, vo);
		vo.setGainWayName(DictHolder.getDictItemName("collectionGainWay", vo.getGainWay()));
		vo.setStateName(DictHolder.getDictItemName("memberHoldCollectionState", vo.getState()));
		if (po.getCollection() != null) {
			vo.setCollectionName(po.getCollection().getName());
			vo.setCollectionCover(po.getCollection().getCover());
			vo.setQuantity(po.getCollection().getQuantity());
		}
		if (po.getMember() != null) {
			vo.setMemberMobile(po.getMember().getMobile());
			vo.setMemberBlockChainAddr(po.getMember().getBlockChainAddr());
		}
		if (po.getIssuedCollection() != null) {
			vo.setUniqueId(po.getIssuedCollection().getUniqueId());
			vo.setCollectionSerialNumber(po.getIssuedCollection().getCollectionSerialNumber());
		}
		return vo;
	}

}
