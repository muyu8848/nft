package com.nft.log.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nft.dictconfig.DictHolder;
import com.nft.log.domain.OperLog;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;

@Data
public class OperLogVO {

	private String id;
	
	private String subSystem;

	private String subSystemName;

	private String module;

	private String operate;

	private String requestMethod;

	private String requestUrl;

	private String requestParam;

	private String ipAddr;

	private String operAccountId;

	private String operName;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date operTime;

	public static List<OperLogVO> convertFor(List<OperLog> operLogs) {
		if (CollectionUtil.isEmpty(operLogs)) {
			return new ArrayList<>();
		}
		List<OperLogVO> vos = new ArrayList<>();
		for (OperLog operLog : operLogs) {
			vos.add(convertFor(operLog));
		}
		return vos;
	}

	public static OperLogVO convertFor(OperLog operLog) {
		if (operLog == null) {
			return null;
		}
		OperLogVO vo = new OperLogVO();
		BeanUtils.copyProperties(operLog, vo);
		vo.setSubSystemName(DictHolder.getDictItemName("subSystem", vo.getSubSystem()));
		return vo;
	}

}
