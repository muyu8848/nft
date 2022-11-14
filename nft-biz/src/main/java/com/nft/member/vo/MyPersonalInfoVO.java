package com.nft.member.vo;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nft.member.domain.Member;

import lombok.Data;

@Data
public class MyPersonalInfoVO {

	private String nickName;
	
	private String realName;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date bindRealNameTime;

	private String avatar;

	private String mobile;

	private String blockChainAddr;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date latelyLoginTime;

	public static MyPersonalInfoVO convertFor(Member po) {
		if (po == null) {
			return null;
		}
		MyPersonalInfoVO vo = new MyPersonalInfoVO();
		BeanUtils.copyProperties(po, vo);
		return vo;
	}

}
