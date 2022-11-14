package com.nft.collection.vo;

import java.util.ArrayList;
import java.util.List;

import com.nft.collection.domain.MysteryBoxCommodity;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;

@Data
public class MysteryBoxCommodityVO {

	private String commodityId;

	private Double probability;

	private String name;

	private String cover;

	public static List<MysteryBoxCommodityVO> convertFor(List<MysteryBoxCommodity> pos) {
		if (CollectionUtil.isEmpty(pos)) {
			return new ArrayList<>();
		}
		List<MysteryBoxCommodityVO> vos = new ArrayList<>();
		for (MysteryBoxCommodity po : pos) {
			vos.add(convertFor(po));
		}
		return vos;
	}

	public static MysteryBoxCommodityVO convertFor(MysteryBoxCommodity po) {
		if (po == null) {
			return null;
		}
		MysteryBoxCommodityVO vo = new MysteryBoxCommodityVO();
		vo.setCommodityId(po.getCommodityId());
		vo.setProbability(po.getProbability());
		if (po.getCommodity() != null) {
			vo.setName(po.getCommodity().getName());
			vo.setCover(po.getCommodity().getCover());
		}
		return vo;
	}

}
