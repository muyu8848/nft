package com.nft.backgroundaccount.param;

import com.nft.common.param.PageParam;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class BackgroundAccountQueryCondParam extends PageParam {
	
	private String userName;

}
