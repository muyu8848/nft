package com.nft.collection.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nft.collection.domain.IssuedCollectionActionLog;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;

@Data
public class IssuedCollectionActionLogVO {

	private String id;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date actionTime;

	private String actionDesc;

	private String memberNickName;

	public static List<IssuedCollectionActionLogVO> convertFor(List<IssuedCollectionActionLog> pos) {
		if (CollectionUtil.isEmpty(pos)) {
			return new ArrayList<>();
		}
		List<IssuedCollectionActionLogVO> vos = new ArrayList<>();
		for (IssuedCollectionActionLog po : pos) {
			vos.add(convertFor(po));
		}
		return vos;
	}

	public static IssuedCollectionActionLogVO convertFor(IssuedCollectionActionLog po) {
		if (po == null) {
			return null;
		}
		IssuedCollectionActionLogVO vo = new IssuedCollectionActionLogVO();
		BeanUtils.copyProperties(po, vo);
		if (po.getMember() != null) {
			vo.setMemberNickName(po.getMember().getNickName());
		}
		return vo;
	}

}
