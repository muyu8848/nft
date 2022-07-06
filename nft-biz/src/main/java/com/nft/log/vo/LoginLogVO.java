package com.nft.log.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nft.dictconfig.DictHolder;
import com.nft.log.domain.LoginLog;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;

@Data
public class LoginLogVO {

	private String id;

	private String sessionId;

	private String subSystem;
	
	private String subSystemName;

	private String state;

	private String stateName;

	private String ipAddr;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date loginTime;

	private String browser;

	private String os;

	private String msg;

	private String userName;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date lastAccessTime;

	public static List<LoginLogVO> convertFor(List<LoginLog> loginLogs) {
		if (CollectionUtil.isEmpty(loginLogs)) {
			return new ArrayList<>();
		}
		List<LoginLogVO> vos = new ArrayList<>();
		for (LoginLog loginLog : loginLogs) {
			vos.add(convertFor(loginLog));
		}
		return vos;
	}

	public static LoginLogVO convertFor(LoginLog loginLog) {
		if (loginLog == null) {
			return null;
		}
		LoginLogVO vo = new LoginLogVO();
		BeanUtils.copyProperties(loginLog, vo);
		vo.setStateName(DictHolder.getDictItemName("loginState", vo.getState()));
		vo.setSubSystemName(DictHolder.getDictItemName("subSystem", vo.getSubSystem()));
		return vo;
	}

}
