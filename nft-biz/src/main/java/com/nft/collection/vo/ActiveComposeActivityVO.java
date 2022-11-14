package com.nft.collection.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nft.collection.domain.ComposeActivity;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;

@Data
public class ActiveComposeActivityVO {

	private String id;

	private String title;

	private Integer quantity;

	private Integer stock;

	@JsonFormat(pattern = "MM-dd HH:mm", timezone = "GMT+8")
	private Date activityTimeEnd;

	public static List<ActiveComposeActivityVO> convertFor(List<ComposeActivity> pos) {
		if (CollectionUtil.isEmpty(pos)) {
			return new ArrayList<>();
		}
		List<ActiveComposeActivityVO> vos = new ArrayList<>();
		for (ComposeActivity po : pos) {
			vos.add(convertFor(po));
		}
		return vos;
	}

	public static ActiveComposeActivityVO convertFor(ComposeActivity po) {
		if (po == null) {
			return null;
		}
		ActiveComposeActivityVO vo = new ActiveComposeActivityVO();
		BeanUtils.copyProperties(po, vo);
		return vo;
	}

}
