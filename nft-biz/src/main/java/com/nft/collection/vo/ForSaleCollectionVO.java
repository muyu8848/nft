package com.nft.collection.vo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.nft.collection.domain.Collection;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;

@Data
public class ForSaleCollectionVO {

	private String id;

	private String name;

	private String cover;

	private Double price;

	private Integer quantity;

	private String creatorName;

	public static List<ForSaleCollectionVO> convertFor(List<Collection> pos) {
		if (CollectionUtil.isEmpty(pos)) {
			return new ArrayList<>();
		}
		List<ForSaleCollectionVO> vos = new ArrayList<>();
		for (Collection po : pos) {
			vos.add(convertFor(po));
		}
		return vos;
	}

	public static ForSaleCollectionVO convertFor(Collection po) {
		if (po == null) {
			return null;
		}
		ForSaleCollectionVO vo = new ForSaleCollectionVO();
		BeanUtils.copyProperties(po, vo);
		if (po.getCreator() != null) {
			vo.setCreatorName(po.getCreator().getName());
		}
		return vo;
	}

}
