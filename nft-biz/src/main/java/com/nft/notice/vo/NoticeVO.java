package com.nft.notice.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nft.notice.domain.Notice;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;

@Data
public class NoticeVO implements Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	private String id;

	private String title;

	private String content;

	private Boolean importantFlag;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date publishTime;

	public static List<NoticeVO> convertFor(List<Notice> pos) {
		if (CollectionUtil.isEmpty(pos)) {
			return new ArrayList<>();
		}
		List<NoticeVO> vos = new ArrayList<>();
		for (Notice po : pos) {
			vos.add(convertFor(po));
		}
		return vos;
	}

	public static NoticeVO convertFor(Notice po) {
		if (po == null) {
			return null;
		}
		NoticeVO vo = new NoticeVO();
		BeanUtils.copyProperties(po, vo);
		return vo;
	}

}
