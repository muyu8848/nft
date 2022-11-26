package com.nft.collection.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nft.collection.domain.IssuedCollection;
import com.nft.collection.domain.PreSaleQualify;
import com.nft.dictconfig.DictHolder;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;

@Data
public class PreSaleQualifyVO {

	private String id;

	private String bizType;

	private String bizTypeName;
	
	private Integer preMinute;

	private String state;

	private String stateName;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date grantTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date dealTime;

	private String taskName;

	private String collectionName;

	private String collectionCover;

	private Integer collectionQuantity;

	private Integer collectionSerialNumber;

	private String memberMobile;

	private String memberBlockChainAddr;

	public static List<PreSaleQualifyVO> convertFor(List<PreSaleQualify> pos) {
		if (CollectionUtil.isEmpty(pos)) {
			return new ArrayList<>();
		}
		List<PreSaleQualifyVO> vos = new ArrayList<>();
		for (PreSaleQualify po : pos) {
			vos.add(convertFor(po));
		}
		return vos;
	}

	public static PreSaleQualifyVO convertFor(PreSaleQualify po) {
		if (po == null) {
			return null;
		}
		PreSaleQualifyVO vo = new PreSaleQualifyVO();
		BeanUtils.copyProperties(po, vo);
		if (po.getPreSaleTask() != null) {
			vo.setTaskName(po.getPreSaleTask().getTaskName());
		}
		if (po.getCollection() != null) {
			vo.setCollectionName(po.getCollection().getName());
			vo.setCollectionCover(po.getCollection().getCover());
			vo.setCollectionQuantity(po.getCollection().getQuantity());
		}
		IssuedCollection issuedCollection = po.getIssuedCollection();
		if (issuedCollection != null) {
			vo.setCollectionSerialNumber(issuedCollection.getCollectionSerialNumber());
		}
		if (po.getMember() != null) {
			vo.setMemberMobile(po.getMember().getMobile());
			vo.setMemberBlockChainAddr(po.getMember().getBlockChainAddr());
		}
		vo.setStateName(DictHolder.getDictItemName("preSaleQualifyState", vo.getState()));
		vo.setBizTypeName(DictHolder.getDictItemName("preSaleBizType", vo.getBizType()));
		return vo;
	}

}
