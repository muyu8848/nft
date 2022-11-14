package com.nft.collection.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nft.collection.domain.MemberResaleCollection;
import com.nft.dictconfig.DictHolder;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;

@Data
public class MemberResaleCollectionVO {

	private String id;

	private String uniqueId;

	private String transactionHash;

	private String collectionName;

	private String collectionCover;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date resaleTime;

	private Double resalePrice;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date soldTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date cancelTime;

	private String state;

	private String stateName;

	private String memberMobile;

	private String memberBlockChainAddr;

	private Integer collectionSerialNumber;

	private Integer quantity;

	public static List<MemberResaleCollectionVO> convertFor(List<MemberResaleCollection> pos) {
		if (CollectionUtil.isEmpty(pos)) {
			return new ArrayList<>();
		}
		List<MemberResaleCollectionVO> vos = new ArrayList<>();
		for (MemberResaleCollection po : pos) {
			vos.add(convertFor(po));
		}
		return vos;
	}

	public static MemberResaleCollectionVO convertFor(MemberResaleCollection po) {
		if (po == null) {
			return null;
		}
		MemberResaleCollectionVO vo = new MemberResaleCollectionVO();
		BeanUtils.copyProperties(po, vo);
		vo.setStateName(DictHolder.getDictItemName("memberResaleCollectionState", vo.getState()));
		if (po.getCollection() != null) {
			vo.setCollectionName(po.getCollection().getName());
			vo.setCollectionCover(po.getCollection().getCover());
			vo.setQuantity(po.getCollection().getQuantity());
		}
		if (po.getMember() != null) {
			vo.setMemberMobile(po.getMember().getMobile());
			vo.setMemberBlockChainAddr(po.getMember().getBlockChainAddr());
		}
		if (po.getMemberHoldCollection() != null) {
			vo.setTransactionHash(po.getMemberHoldCollection().getTransactionHash());
		}
		if (po.getIssuedCollection() != null) {
			vo.setUniqueId(po.getIssuedCollection().getUniqueId());
			vo.setCollectionSerialNumber(po.getIssuedCollection().getCollectionSerialNumber());
		}
		return vo;
	}

}
