package com.nft.member.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nft.dictconfig.DictHolder;
import com.nft.member.domain.Member;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;

@Data
public class MemberVO {

	private String id;

	private String realName;

	private String identityCard;

	private String mobile;

	private String nickName;

	private String avatar;

	private String blockChainAddr;

	private String state;

	private String stateName;

	private Double balance;

	private Boolean boughtFlag;

	private String inviteCode;

	private Boolean notSetPayPwd;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date registeredTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date latelyLoginTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date bindRealNameTime;

	private String inviterMobile;

	public static List<MemberVO> convertFor(List<Member> pos) {
		if (CollectionUtil.isEmpty(pos)) {
			return new ArrayList<>();
		}
		List<MemberVO> vos = new ArrayList<>();
		for (Member po : pos) {
			vos.add(convertFor(po));
		}
		return vos;
	}

	public static MemberVO convertFor(Member po) {
		if (po == null) {
			return null;
		}
		MemberVO vo = new MemberVO();
		BeanUtils.copyProperties(po, vo);
		vo.setNotSetPayPwd(StrUtil.isBlank(po.getPayPwd()));
		vo.setStateName(DictHolder.getDictItemName("functionState", vo.getState()));
		if (po.getInviter() != null) {
			vo.setInviterMobile(po.getInviter().getMobile());
		}
		return vo;
	}

}
