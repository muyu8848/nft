package com.nft.backgroundaccount.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nft.backgroundaccount.domain.BackgroundAccount;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;

@Data
public class SuperAdminVO {

	private String id;

	private String userName;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date latelyLoginTime;

	public static List<SuperAdminVO> convertFor(List<BackgroundAccount> userAccounts) {
		if (CollectionUtil.isEmpty(userAccounts)) {
			return new ArrayList<>();
		}
		List<SuperAdminVO> vos = new ArrayList<>();
		for (BackgroundAccount userAccount : userAccounts) {
			vos.add(convertFor(userAccount));
		}
		return vos;
	}

	public static SuperAdminVO convertFor(BackgroundAccount userAccount) {
		if (userAccount == null) {
			return null;
		}
		SuperAdminVO vo = new SuperAdminVO();
		BeanUtils.copyProperties(userAccount, vo);
		return vo;
	}

}
