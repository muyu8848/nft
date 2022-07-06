package com.nft.backgroundaccount.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nft.backgroundaccount.domain.BackgroundAccount;
import com.nft.dictconfig.DictHolder;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;

@Data
public class BackgroundAccountVO {

	private String id;

	private String userName;

	private String state;

	private String stateName;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date registeredTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date latelyLoginTime;

	private String googleSecretKey;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date googleAuthBindTime;
	
	private Boolean superAdminFlag;

	public static List<BackgroundAccountVO> convertFor(List<BackgroundAccount> userAccounts) {
		if (CollectionUtil.isEmpty(userAccounts)) {
			return new ArrayList<>();
		}
		List<BackgroundAccountVO> vos = new ArrayList<>();
		for (BackgroundAccount userAccount : userAccounts) {
			vos.add(convertFor(userAccount));
		}
		return vos;
	}

	public static BackgroundAccountVO convertFor(BackgroundAccount userAccount) {
		if (userAccount == null) {
			return null;
		}
		BackgroundAccountVO vo = new BackgroundAccountVO();
		BeanUtils.copyProperties(userAccount, vo);
		vo.setStateName(DictHolder.getDictItemName("functionState", vo.getState()));
		return vo;
	}

}
