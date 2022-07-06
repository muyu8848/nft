package com.nft.common;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;

@Component
public class StpInterfaceImpl implements StpInterface {

	@Override
	public List<String> getPermissionList(Object loginId, String loginType) {
		List<String> list = new ArrayList<String>();
		return list;
	}

	@Override
	public List<String> getRoleList(Object loginId, String loginType) {
		List<String> list = new ArrayList<String>();
		String tokenName = StpUtil.getTokenName();
		if ("admin_token".equals(tokenName)) {
			list.add("admin");
		} else if ("member_token".equals(tokenName)) {
			list.add("member");
		} else if ("merchant_token".equals(tokenName)) {
			list.add("merchant");
		}
		return list;
	}

}
