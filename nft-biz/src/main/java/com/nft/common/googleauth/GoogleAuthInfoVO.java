package com.nft.common.googleauth;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class GoogleAuthInfoVO {

	private String userName;

	private String googleSecretKey;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date googleAuthBindTime;

	public static GoogleAuthInfoVO convertFor(String userName, String googleSecretKey, Date googleAuthBindTime) {
		GoogleAuthInfoVO vo = new GoogleAuthInfoVO();
		vo.setUserName(userName);
		vo.setGoogleSecretKey(googleSecretKey);
		vo.setGoogleAuthBindTime(googleAuthBindTime);
		return vo;
	}

}
