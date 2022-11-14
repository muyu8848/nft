package com.nft.collection.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nft.collection.domain.Collection;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import lombok.Data;

@Data
public class LatestCollectionVO {

	private String id;

	private String name;

	private String cover;

	private Double price;

	private Integer quantity;

	private Integer stock;

	@JsonFormat(pattern = "MM.dd HH:mm", timezone = "GMT+8")
	private Date saleTime;

	private Long surplusSecond;
	
	private String creatorName;

	private String creatorAvatar;

	public static List<LatestCollectionVO> convertFor(List<Collection> pos) {
		if (CollectionUtil.isEmpty(pos)) {
			return new ArrayList<>();
		}
		List<LatestCollectionVO> vos = new ArrayList<>();
		for (Collection po : pos) {
			vos.add(convertFor(po));
		}
		return vos;
	}

	public static LatestCollectionVO convertFor(Collection po) {
		if (po == null) {
			return null;
		}
		LatestCollectionVO vo = new LatestCollectionVO();
		BeanUtils.copyProperties(po, vo);
		long surplusSecond = DateUtil.between(new Date(), po.getSaleTime(), DateUnit.SECOND, false);
		vo.setSurplusSecond(surplusSecond > 0 ? surplusSecond : 0);
		if (po.getCreator() != null) {
			vo.setCreatorName(po.getCreator().getName());
			vo.setCreatorAvatar(po.getCreator().getAvatar());
		}
		return vo;
	}

}
