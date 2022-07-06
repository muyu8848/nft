package com.nft.backgroundaccount.vo;

import org.springframework.beans.BeanUtils;

import com.nft.backgroundaccount.domain.BackgroundAccount;

import lombok.Data;

@Data
public class AccountAuthInfoVO {

	private String id;

	private String userName;

	private String loginPwd;

	private String googleSecretKey;

	private String state;
	
	private Boolean superAdminFlag;

	public static AccountAuthInfoVO convertFor(BackgroundAccount userAccount) {
		if (userAccount == null) {
			return null;
		}
		AccountAuthInfoVO vo = new AccountAuthInfoVO();
		BeanUtils.copyProperties(userAccount, vo);
		return vo;
	}

}
