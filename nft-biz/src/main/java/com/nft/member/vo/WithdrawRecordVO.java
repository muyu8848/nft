package com.nft.member.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nft.dictconfig.DictHolder;
import com.nft.member.domain.WithdrawRecord;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;

@Data
public class WithdrawRecordVO {

	private String id;

	private String orderNo;

	private Double amount;

	private Double handlingFee;

	private Double toTheAccount;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date submitTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date dealTime;

	private String state;

	private String stateName;

	private String note;

	private SettlementAccountVO settlementAccount;

	private String memberMobile;

	private String memberBlockChainAddr;

	public static List<WithdrawRecordVO> convertFor(List<WithdrawRecord> pos) {
		if (CollectionUtil.isEmpty(pos)) {
			return new ArrayList<>();
		}
		List<WithdrawRecordVO> vos = new ArrayList<>();
		for (WithdrawRecord po : pos) {
			vos.add(convertFor(po));
		}
		return vos;
	}

	public static WithdrawRecordVO convertFor(WithdrawRecord po) {
		if (po == null) {
			return null;
		}
		WithdrawRecordVO vo = new WithdrawRecordVO();
		BeanUtils.copyProperties(po, vo);
		vo.setStateName(DictHolder.getDictItemName("withdrawRecordState", vo.getState()));
		if (po.getSettlementAccount() != null) {
			vo.setSettlementAccount(SettlementAccountVO.convertFor(po.getSettlementAccount()));
		}
		if (po.getMember() != null) {
			vo.setMemberMobile(po.getMember().getMobile());
			vo.setMemberBlockChainAddr(po.getMember().getBlockChainAddr());
		}
		return vo;
	}

}
