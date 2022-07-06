package com.nft.collection.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nft.collection.domain.Collection;
import com.nft.collection.domain.CollectionGiveRecord;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;

@Data
public class CollectionGiveRecordVO {

	private String id;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date giveTime;

	private String collectionName;
	
	private String collectionCover;

	private String giveFromMobile;
	
	private String giveFromBlockChainAddr;

	private String giveToMobile;
	
	private String giveToBlockChainAddr;

	public static List<CollectionGiveRecordVO> convertFor(List<CollectionGiveRecord> pos) {
		if (CollectionUtil.isEmpty(pos)) {
			return new ArrayList<>();
		}
		List<CollectionGiveRecordVO> vos = new ArrayList<>();
		for (CollectionGiveRecord po : pos) {
			vos.add(convertFor(po));
		}
		return vos;
	}

	public static CollectionGiveRecordVO convertFor(CollectionGiveRecord po) {
		if (po == null) {
			return null;
		}
		CollectionGiveRecordVO vo = new CollectionGiveRecordVO();
		BeanUtils.copyProperties(po, vo);
		if (po.getHoldCollection() != null) {
			Collection collection = po.getHoldCollection().getCollection();
			if (collection != null) {
				vo.setCollectionName(collection.getName());
				vo.setCollectionCover(collection.getCover());
			}
		}
		if (po.getGiveFrom() != null) {
			vo.setGiveFromMobile(po.getGiveFrom().getMobile());
			vo.setGiveFromBlockChainAddr(po.getGiveFrom().getBlockChainAddr());
		}
		if (po.getGiveTo() != null) {
			vo.setGiveToMobile(po.getGiveTo().getMobile());
			vo.setGiveToBlockChainAddr(po.getGiveTo().getBlockChainAddr());
		}
		return vo;
	}

}
