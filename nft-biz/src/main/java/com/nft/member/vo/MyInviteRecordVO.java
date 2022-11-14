package com.nft.member.vo;

import java.util.ArrayList;
import java.util.List;

import com.nft.member.domain.Member;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.DesensitizedUtil;
import lombok.Data;

@Data
public class MyInviteRecordVO {

	private String mobile;

	private Boolean inviteSuccessFlag;

	private Boolean boughtFlag;

	public static List<MyInviteRecordVO> convertFor(List<Member> pos) {
		if (CollectionUtil.isEmpty(pos)) {
			return new ArrayList<>();
		}
		List<MyInviteRecordVO> vos = new ArrayList<>();
		for (Member po : pos) {
			vos.add(convertFor(po));
		}
		return vos;
	}

	public static MyInviteRecordVO convertFor(Member po) {
		if (po == null) {
			return null;
		}
		MyInviteRecordVO vo = new MyInviteRecordVO();
		vo.setMobile(DesensitizedUtil.mobilePhone(po.getMobile()));
		vo.setInviteSuccessFlag(po.getBindRealNameTime() != null);
		vo.setBoughtFlag(po.getBoughtFlag());
		return vo;
	}

}
