package com.nft.member.vo;

import org.springframework.beans.BeanUtils;

import com.nft.member.domain.Member;

import lombok.Data;

@Data
public class AccountAuthInfoVO {

	private String id;

	private String mobile;

	private String loginPwd;

	private String state;
	
	private Integer keepLoginDuration;
	
	public static AccountAuthInfoVO convertFor(Member member) {
		if (member == null) {
			return null;
		}
		AccountAuthInfoVO vo = new AccountAuthInfoVO();
		BeanUtils.copyProperties(member, vo);
		return vo;
	}

}
