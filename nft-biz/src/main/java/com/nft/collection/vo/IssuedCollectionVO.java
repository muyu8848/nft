package com.nft.collection.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nft.collection.domain.IssuedCollection;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;

@Data
public class IssuedCollectionVO {

	private String id;
	
	private Integer collectionSerialNumber;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date issueTime;

	private String uniqueId;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date syncChainTime;

	public static List<IssuedCollectionVO> convertFor(List<IssuedCollection> pos) {
		if (CollectionUtil.isEmpty(pos)) {
			return new ArrayList<>();
		}
		List<IssuedCollectionVO> vos = new ArrayList<>();
		for (IssuedCollection po : pos) {
			vos.add(convertFor(po));
		}
		return vos;
	}

	public static IssuedCollectionVO convertFor(IssuedCollection po) {
		if (po == null) {
			return null;
		}
		IssuedCollectionVO vo = new IssuedCollectionVO();
		BeanUtils.copyProperties(po, vo);
		return vo;
	}

}
