package com.nft.dictconfig.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.nft.dictconfig.domain.DictItem;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;

@Data
public class DictItemVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String dictItemCode;

	private String dictItemName;


	public static List<DictItemVO> convertFor(List<DictItem> dictItems) {
		if (CollectionUtil.isEmpty(dictItems)) {
			return new ArrayList<>();
		}
		List<DictItemVO> vos = new ArrayList<>();
		for (DictItem dictItem : dictItems) {
			vos.add(convertFor(dictItem));
		}
		return vos;
	}

	public static DictItemVO convertFor(DictItem dictItem) {
		if (dictItem == null) {
			return null;
		}
		DictItemVO vo = new DictItemVO();
		BeanUtils.copyProperties(dictItem, vo);
		return vo;
	}

}
