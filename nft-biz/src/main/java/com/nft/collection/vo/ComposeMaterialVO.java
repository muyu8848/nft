package com.nft.collection.vo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.nft.collection.domain.ComposeMaterial;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;

@Data
public class ComposeMaterialVO {

	private Integer quantity;

	private String materialId;

	private String materialName;
	
	private String materialCover;

	public static List<ComposeMaterialVO> convertFor(List<ComposeMaterial> pos) {
		if (CollectionUtil.isEmpty(pos)) {
			return new ArrayList<>();
		}
		List<ComposeMaterialVO> vos = new ArrayList<>();
		for (ComposeMaterial po : pos) {
			vos.add(convertFor(po));
		}
		return vos;
	}

	public static ComposeMaterialVO convertFor(ComposeMaterial po) {
		if (po == null) {
			return null;
		}
		ComposeMaterialVO vo = new ComposeMaterialVO();
		BeanUtils.copyProperties(po, vo);
		if (po.getMaterial() != null) {
			vo.setMaterialName(po.getMaterial().getName());
			vo.setMaterialCover(po.getMaterial().getCover());
		}
		return vo;
	}

}
