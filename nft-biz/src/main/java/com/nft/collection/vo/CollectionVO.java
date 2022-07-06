package com.nft.collection.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nft.collection.domain.Collection;
import com.nft.collection.domain.CollectionStory;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;

@Data
public class CollectionVO {

	private String id;

	private String name;

	private String cover;

	private Double price;

	private Integer quantity;

	private Integer stock;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	private Date saleTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	private String creatorName;

	private List<CollectionStoryVO> collectionStorys = new ArrayList<>();

	public static List<CollectionVO> convertFor(List<Collection> pos) {
		if (CollectionUtil.isEmpty(pos)) {
			return new ArrayList<>();
		}
		List<CollectionVO> vos = new ArrayList<>();
		for (Collection po : pos) {
			vos.add(convertFor(po));
		}
		return vos;
	}

	public static CollectionVO convertFor(Collection po) {
		if (po == null) {
			return null;
		}
		CollectionVO vo = new CollectionVO();
		BeanUtils.copyProperties(po, vo);
		if (po.getCreator() != null) {
			vo.setCreatorName(po.getCreator().getName());
		}
		Set<CollectionStory> collectionStorys = po.getCollectionStorys();
		for (CollectionStory collectionStory : collectionStorys) {
			vo.getCollectionStorys().add(new CollectionStoryVO(collectionStory.getPicLink()));
		}
		return vo;
	}

}
