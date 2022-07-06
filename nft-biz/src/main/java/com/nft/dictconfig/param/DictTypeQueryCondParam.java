package com.nft.dictconfig.param;

import com.nft.common.param.PageParam;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DictTypeQueryCondParam extends PageParam {
	
	private String dictTypeCode;

	private String dictTypeName;

}
