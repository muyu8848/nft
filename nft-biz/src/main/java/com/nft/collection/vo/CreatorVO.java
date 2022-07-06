package com.nft.collection.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nft.collection.domain.Creator;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;

@Data
public class CreatorVO {

	private String id;

	private String name;

	private String avatar;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date lastModifyTime;

	public static List<CreatorVO> convertFor(List<Creator> pos) {
		if (CollectionUtil.isEmpty(pos)) {
			return new ArrayList<>();
		}
		List<CreatorVO> vos = new ArrayList<>();
		for (Creator po : pos) {
			vos.add(convertFor(po));
		}
		return vos;
	}

	public static CreatorVO convertFor(Creator po) {
		if (po == null) {
			return null;
		}
		CreatorVO vo = new CreatorVO();
		BeanUtils.copyProperties(po, vo);
		return vo;
	}

}
