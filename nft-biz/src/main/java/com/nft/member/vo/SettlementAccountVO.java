package com.nft.member.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nft.dictconfig.DictHolder;
import com.nft.member.domain.SettlementAccount;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;

@Data
public class SettlementAccountVO {

	private String id;

	private String realName;

	private String type;

	private String typeName;

	private Boolean activated;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date activatedTime;

	private String cardNumber;

	private String bankName;

	private String account;

	public static List<SettlementAccountVO> convertFor(List<SettlementAccount> pos) {
		if (CollectionUtil.isEmpty(pos)) {
			return new ArrayList<>();
		}
		List<SettlementAccountVO> vos = new ArrayList<>();
		for (SettlementAccount po : pos) {
			vos.add(convertFor(po));
		}
		return vos;
	}

	public static SettlementAccountVO convertFor(SettlementAccount po) {
		if (po == null) {
			return null;
		}
		SettlementAccountVO vo = new SettlementAccountVO();
		BeanUtils.copyProperties(po, vo);
		vo.setTypeName(DictHolder.getDictItemName("settlementAccountType", vo.getType()));
		return vo;
	}

}
