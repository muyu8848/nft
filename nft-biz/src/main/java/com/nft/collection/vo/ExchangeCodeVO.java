package com.nft.collection.vo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.nft.collection.domain.ExchangeCode;
import com.nft.dictconfig.DictHolder;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;

@Data
public class ExchangeCodeVO {

	private String id;

	private String code;

	private String state;

	private String stateName;

	public static List<ExchangeCodeVO> convertFor(List<ExchangeCode> pos) {
		if (CollectionUtil.isEmpty(pos)) {
			return new ArrayList<>();
		}
		List<ExchangeCodeVO> vos = new ArrayList<>();
		for (ExchangeCode po : pos) {
			vos.add(convertFor(po));
		}
		return vos;
	}

	public static ExchangeCodeVO convertFor(ExchangeCode po) {
		if (po == null) {
			return null;
		}
		ExchangeCodeVO vo = new ExchangeCodeVO();
		BeanUtils.copyProperties(po, vo);
		vo.setStateName(DictHolder.getDictItemName("exchangeCodeState", vo.getState()));
		return vo;
	}

}
