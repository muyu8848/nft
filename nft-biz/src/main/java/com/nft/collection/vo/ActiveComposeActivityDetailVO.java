package com.nft.collection.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;

import com.nft.collection.domain.ComposeActivity;
import com.nft.collection.domain.ComposeMaterial;

import lombok.Data;

@Data
public class ActiveComposeActivityDetailVO {

	private String id;
	
	private String title;

	private String collectionName;

	private String collectionCover;

	private List<ComposeMaterialVO> materials = new ArrayList<>();

	public static ActiveComposeActivityDetailVO convertFor(ComposeActivity po) {
		if (po == null) {
			return null;
		}
		ActiveComposeActivityDetailVO vo = new ActiveComposeActivityDetailVO();
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
