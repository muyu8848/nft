package com.nft.collection.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nft.collection.domain.PreSaleCondition;
import com.nft.collection.domain.PreSaleTask;
import com.nft.dictconfig.DictHolder;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;

@Data
public class PreSaleTaskVO {

	private String id;

	private String taskName;

	private String state;

	private String stateName;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	private Date executeTime;

	private Integer preMinute;

	private String collectionName;

	private String collectionCover;

	private List<PreSaleConditionVO> preSaleConditions = new ArrayList<>();

	public static List<PreSaleTaskVO> convertFor(List<PreSaleTask> pos) {
		if (CollectionUtil.isEmpty(pos)) {
			return new ArrayList<>();
		}
		List<PreSaleTaskVO> vos = new ArrayList<>();
		for (PreSaleTask po : pos) {
			vos.add(convertFor(po));
		}
		return vos;
	}

	public static PreSaleTaskVO convertFor(PreSaleTask po) {
		if (po == null) {
			return null;
		}
		PreSaleTaskVO vo = new PreSaleTaskVO();
		BeanUtils.copyProperties(po, vo);
		if (po.getCollection() != null) {
			vo.setCollectionName(po.getCollection().getName());
			vo.setCollectionCover(po.getCollection().getCover());
		}
		Set<PreSaleCondition> conditions = po.getPreSaleConditions();
		for (PreSaleCondition condition : conditions) {
			vo.getPreSaleConditions().add(PreSaleConditionVO.convertFor(condition));
		}
		vo.setStateName(DictHolder.getDictItemName("preSaleTaskState", vo.getState()));
		return vo;
	}

}
