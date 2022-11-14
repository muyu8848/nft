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
public class MemberFinanceRecordVO {

	private String id;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date changeTime;

	private String changeType;

	private String changeTypeName;

	private Double balanceChange;

	public static List<MemberFinanceRecordVO> convertFor(List<MemberBalanceChangeLog> pos) {
		if (CollectionUtil.isEmpty(pos)) {
			return new ArrayList<>();
		}
		List<MemberFinanceRecordVO> vos = new ArrayList<>();
		for (MemberBalanceChangeLog po : pos) {
			vos.add(convertFor(po));
		}
		return vos;
	}

	public static MemberFinanceRecordVO convertFor(MemberBalanceChangeLog po) {
		if (po == null) {
			return null;
		}
		MemberFinanceRecordVO vo = new MemberFinanceRecordVO();
		BeanUtils.copyProperties(po, vo);
		vo.setChangeTypeName(DictHolder.getDictItemName("memberBalanceChangeType", vo.getChangeType()));
		return vo;
	}

}
