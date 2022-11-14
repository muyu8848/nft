package com.nft.notice.param;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import com.nft.common.utils.IdUtils;
import com.nft.notice.domain.Notice;

import lombok.Data;

@Data
public class AddOrUpdateNoticeParam {

	private String id;

	@NotBlank
	private String title;

	@NotBlank
	private String content;

	@NotBlank
	private String type;

	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date publishTime;

	public Notice convertToPo() {
		Notice po = new Notice();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setLastModifyTime(new Date());
		po.setDeletedFlag(false);
		return po;
	}

}
