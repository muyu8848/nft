package com.nft.notice.param;

import com.nft.common.param.PageParam;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class NoticeQueryCondParam extends PageParam {
	
	private String title;

}
