package com.nft.common.vo;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import lombok.Data;

@Data
public class TokenInfo {

	private String tokenName;

	private String tokenValue;
	
	private String accountId;

	public static TokenInfo build() {
		SaTokenInfo saTokenInfo = StpUtil.getTokenInfo();
		TokenInfo tokenInfo = new TokenInfo();
		tokenInfo.setTokenName(saTokenInfo.getTokenName());
		tokenInfo.setTokenValue(saTokenInfo.getTokenValue());
		return tokenInfo;
	}

}
