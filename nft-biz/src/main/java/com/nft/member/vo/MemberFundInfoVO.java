package com.nft.member.vo;

import java.util.List;

import org.springframework.beans.BeanUtils;

import com.nft.member.domain.Member;

import cn.hutool.core.util.NumberUtil;
import lombok.Data;

@Data
public class MemberFundInfoVO {

	private Double balance = 0d;

	private Double freezeFund = 0d;

	private Double totalFund = 0d;

	private String walletAddr;

	public static MemberFundInfoVO convertFor(Member po) {
		if (po == null) {
			return null;
		}
		MemberFundInfoVO vo = new MemberFundInfoVO();
		BeanUtils.copyProperties(po, vo);
		vo.setTotalFund(NumberUtil.round(vo.getBalance() + vo.getFreezeFund(), 2).doubleValue());
		return vo;
	}

	public static MemberFundInfoVO convertFor(List<Member> pos) {
		MemberFundInfoVO vo = new MemberFundInfoVO();
		for (Member po : pos) {
			vo.setBalance(NumberUtil.round(vo.getBalance() + po.getBalance(), 2).doubleValue());
		}
		vo.setTotalFund(NumberUtil.round(vo.getBalance() + vo.getFreezeFund(), 2).doubleValue());
		return vo;
	}

}
