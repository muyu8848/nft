package com.nft.notice.param;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.nft.common.utils.IdUtils;
import com.nft.notice.domain.Notice;

import lombok.Data;

@Data
public class AddOrUpdateNoticeParam {

	private String id;

	private String title;

	private Boolean importantFlag;

	private String content;

	public Notice convertToPo() {
		Notice po = new Notice();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setPublishTime(new Date());
		po.setDeletedFlag(false);
		return po;
	}

}
