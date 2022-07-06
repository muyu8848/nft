package com.nft.sms.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nft.dictconfig.DictHolder;
import com.nft.sms.domain.SmsSendRecord;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;

@Data
public class SmsSendRecordVO {

	private String id;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date sendTime;

	private String smsType;

	private String smsTypeName;

	private String mobile;

	private String verificationCode;

	private String errorMsg;

	private String state;

	private String stateName;

	public static List<SmsSendRecordVO> convertFor(List<SmsSendRecord> pos) {
		if (CollectionUtil.isEmpty(pos)) {
			return new ArrayList<>();
		}
		List<SmsSendRecordVO> vos = new ArrayList<>();
		for (SmsSendRecord po : pos) {
			vos.add(convertFor(po));
		}
		return vos;
	}

	public static SmsSendRecordVO convertFor(SmsSendRecord po) {
		if (po == null) {
			return null;
		}
		SmsSendRecordVO vo = new SmsSendRecordVO();
		BeanUtils.copyProperties(po, vo);
		vo.setSmsTypeName(DictHolder.getDictItemName("smsType", vo.getSmsType()));
		vo.setStateName(DictHolder.getDictItemName("smsSendState", vo.getState()));
		return vo;
	}

}
