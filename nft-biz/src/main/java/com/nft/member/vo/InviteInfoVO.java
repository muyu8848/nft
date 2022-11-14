package com.nft.member.vo;

import lombok.Data;

@Data
public class InviteInfoVO {

	private String inviteCode;

	private String inviteLink;

	public static InviteInfoVO build(String inviteCode, String h5Gateway) {
		InviteInfoVO vo = new InviteInfoVO();
		vo.setInviteCode(inviteCode);
		vo.setInviteLink(h5Gateway + "/#/pages/login/login?inviteCode=" + inviteCode);
		return vo;
	}

}
