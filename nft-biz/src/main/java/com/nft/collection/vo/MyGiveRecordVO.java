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
public class MyGiveRecordVO {

	private String id;

	private String orderNo;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date giveTime;

	private String giveDirection;

	private String collectionName;

	private String collectionCover;

	private String giveFromMobile;

	private String giveToMobile;

	public static List<MyGiveRecordVO> convertFor(List<CollectionGiveRecord> pos, String memberId) {
		if (CollectionUtil.isEmpty(pos)) {
			return new ArrayList<>();
		}
		List<MyGiveRecordVO> vos = new ArrayList<>();
		for (CollectionGiveRecord po : pos) {
			vos.add(convertFor(po, memberId));
		}
		return vos;
	}

	public static MyGiveRecordVO convertFor(CollectionGiveRecord po, String memberId) {
		if (po == null) {
			return null;
		}
		MyGiveRecordVO vo = new MyGiveRecordVO();
		BeanUtils.copyProperties(po, vo);
		if (po.getHoldCollection() != null) {
			Collection collection = po.getHoldCollection().getCollection();
			if (collection != null) {
				vo.setCollectionName(collection.getName());
				vo.setCollectionCover(collection.getCover());
			}
		}
		vo.setGiveDirection(po.getGiveFromId().equals(memberId) ? "from" : "to");
		if (po.getGiveFrom() != null) {
			vo.setGiveFromMobile(po.getGiveFrom().getMobile());
		}
		if (po.getGiveTo() != null) {
			vo.setGiveToMobile(po.getGiveTo().getMobile());
		}
		return vo;
	}

}
