package com.nft.collection.vo;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nft.collection.domain.Collection;
import com.nft.collection.domain.Creator;
import com.nft.collection.domain.PayOrder;
import com.nft.dictconfig.DictHolder;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import lombok.Data;

@Data
public class MyPayOrderDetailVO {

	private String id;

	private String orderNo;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date paidTime;

	private Long surplusSecond;

	private Double amount;

	private String state;

	private String stateName;

	private String collectionName;

	private String collectionCover;

	private String creatorName;

	public static MyPayOrderDetailVO convertFor(PayOrder po) {
		if (po == null) {
			return null;
		}
		MyPayOrderDetailVO vo = new MyPayOrderDetailVO();
		BeanUtils.copyProperties(po, vo);
		if (po.getCollection() != null) {
			Collection collection = po.getCollection();
			if (collection != null) {
				vo.setCollectionName(collection.getName());
				vo.setCollectionCover(collection.getCover());
			}
			Creator creator = collection.getCreator();
			if (creator != null) {
				vo.setCreatorName(creator.getName());
			}
		}
		long surplusSecond = DateUtil.between(new Date(), po.getOrderDeadline(), DateUnit.SECOND, false);
		vo.setSurplusSecond(surplusSecond > 0 ? surplusSecond : 0);
		vo.setStateName(DictHolder.getDictItemName("payOrderState", vo.getState()));
		return vo;
	}

}
