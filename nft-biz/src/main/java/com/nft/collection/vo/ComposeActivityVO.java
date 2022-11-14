package com.nft.collection.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nft.collection.domain.ComposeActivity;
import com.nft.collection.domain.ComposeMaterial;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;

@Data
public class ComposeActivityVO {

	private String id;

	private String title;

	private Integer quantity;

	private Integer stock;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	private Date activityTimeStart;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	private Date activityTimeEnd;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	private String collectionName;

	private String collectionCover;

	private List<ComposeMaterialVO> materials = new ArrayList<>();

	public static List<ComposeActivityVO> convertFor(List<ComposeActivity> pos) {
		if (CollectionUtil.isEmpty(pos)) {
			return new ArrayList<>();
		}
		List<ComposeActivityVO> vos = new ArrayList<>();
		for (ComposeActivity po : pos) {
			vos.add(convertFor(po));
		}
		return vos;
	}

	public static ComposeActivityVO convertFor(ComposeActivity po) {
		if (po == null) {
			return null;
		}
		ComposeActivityVO vo = new ComposeActivityVO();
		BeanUtils.copyProperties(po, vo);
		if (po.getCollection() != null) {
			vo.setCollectionName(po.getCollection().getName());
			vo.setCollectionCover(po.getCollection().getCover());
		}
		Set<ComposeMaterial> materials = po.getMaterials();
		for (ComposeMaterial material : materials) {
			vo.getMaterials().add(ComposeMaterialVO.convertFor(material));
		}
		return vo;
	}

}
