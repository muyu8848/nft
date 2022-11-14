package com.nft.collection.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nft.collection.domain.Collection;
import com.nft.collection.domain.Creator;
import com.nft.collection.domain.PayOrder;
import com.nft.dictconfig.DictHolder;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;

@Data
public class MyPayOrderVO {

	private String id;

	private String orderNo;
	
	private String commodityType;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	private Double amount;

	private String state;

	private String stateName;

	private String collectionName;

	private String collectionCover;

	private String creatorName;

	public static List<MyPayOrderVO> convertFor(List<PayOrder> pos) {
		if (CollectionUtil.isEmpty(pos)) {
			return new ArrayList<>();
		}
		List<MyPayOrderVO> vos = new ArrayList<>();
		for (PayOrder po : pos) {
			vos.add(convertFor(po));
		}
		return vos;
	}

	public static MyPayOrderVO convertFor(PayOrder po) {
		if (po == null) {
			return null;
		}
		MyPayOrderVO vo = new MyPayOrderVO();
		BeanUtils.copyProperties(po, vo);
		if (po.getCollection() != null) {
			Collection collection = po.getCollection();
			if (collection != null) {
				vo.setCommodityType(collection.getCommodityType());
				vo.setCollectionName(collection.getName());
				vo.setCollectionCover(collection.getCover());
			}
			Creator creator = collection.getCreator();
			if (creator != null) {
				vo.setCreatorName(creator.getName());
			}
		}
		vo.setStateName(DictHolder.getDictItemName("payOrderState", vo.getState()));
		return vo;
	}

}
