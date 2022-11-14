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
public class PayOrderVO {

	private String id;

	private String orderNo;

	private String commodityType;

	private String commodityTypeName;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date paidTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date cancelTime;

	private Double amount;

	private String bizMode;

	private String bizModeName;

	private String bizOrderNo;

	private String state;

	private String stateName;

	private String collectionName;

	private String collectionCover;

	private String creatorName;

	private String memberMobile;

	private String memberBlockChainAddr;

	public static List<PayOrderVO> convertFor(List<PayOrder> pos) {
		if (CollectionUtil.isEmpty(pos)) {
			return new ArrayList<>();
		}
		List<PayOrderVO> vos = new ArrayList<>();
		for (PayOrder po : pos) {
			vos.add(convertFor(po));
		}
		return vos;
	}

	public static PayOrderVO convertFor(PayOrder po) {
		if (po == null) {
			return null;
		}
		PayOrderVO vo = new PayOrderVO();
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
		if (po.getMember() != null) {
			vo.setMemberMobile(po.getMember().getMobile());
			vo.setMemberBlockChainAddr(po.getMember().getBlockChainAddr());
		}
		vo.setStateName(DictHolder.getDictItemName("payOrderState", vo.getState()));
		vo.setBizModeName(DictHolder.getDictItemName("payOrderBizMode", vo.getBizMode()));
		vo.setCommodityTypeName(DictHolder.getDictItemName("commodityType", vo.getCommodityType()));
		return vo;
	}

}
