package com.nft.log.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nft.dictconfig.DictHolder;
import com.nft.log.domain.MemberBalanceChangeLog;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;

@Data
public class MemberBalanceChangeLogVO {

	private String id;

	private String bizOrderNo;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date changeTime;

	private String changeType;

	private String changeTypeName;

	private Double balanceChange;

	private Double balanceBefore;

	private Double balanceAfter;

	private String mobile;

	private String realName;

	public static List<MemberBalanceChangeLogVO> convertFor(List<MemberBalanceChangeLog> pos) {
		if (CollectionUtil.isEmpty(pos)) {
			return new ArrayList<>();
		}
		List<MemberBalanceChangeLogVO> vos = new ArrayList<>();
		for (MemberBalanceChangeLog po : pos) {
			vos.add(convertFor(po));
		}
		return vos;
	}

	public static MemberBalanceChangeLogVO convertFor(MemberBalanceChangeLog po) {
		if (po == null) {
			return null;
		}
		MemberBalanceChangeLogVO vo = new MemberBalanceChangeLogVO();
		BeanUtils.copyProperties(po, vo);
		if (po.getMember() != null) {
			vo.setMobile(po.getMember().getMobile());
			vo.setRealName(po.getMember().getRealName());
		}
		vo.setChangeTypeName(DictHolder.getDictItemName("memberBalanceChangeType", vo.getChangeType()));
		return vo;
	}

}
