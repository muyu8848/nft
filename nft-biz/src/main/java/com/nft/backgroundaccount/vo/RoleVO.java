package com.nft.backgroundaccount.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nft.backgroundaccount.domain.Role;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;

@Data
public class RoleVO {

	private String id;

	private String name;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	public static List<RoleVO> convertFor(List<Role> roles) {
		if (CollectionUtil.isEmpty(roles)) {
			return new ArrayList<>();
		}
		List<RoleVO> vos = new ArrayList<>();
		for (Role role : roles) {
			vos.add(convertFor(role));
		}
		return vos;
	}

	public static RoleVO convertFor(Role role) {
		if (role == null) {
			return null;
		}
		RoleVO vo = new RoleVO();
		BeanUtils.copyProperties(role, vo);
		return vo;
	}

}
