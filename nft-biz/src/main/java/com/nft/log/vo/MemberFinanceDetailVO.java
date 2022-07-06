package com.nft.log.vo;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nft.dictconfig.DictHolder;
import com.nft.log.domain.MemberBalanceChangeLog;

import lombok.Data;

@Data
public class MemberFinanceDetailVO {

	private String id;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date changeTime;

	private String changeType;

	private String changeTypeName;

	private Double balanceChange;

	public static MemberFinanceDetailVO convertFor(MemberBalanceChangeLog po) {
		if (po == null) {
			return null;
		}
		MemberFinanceDetailVO vo = new MemberFinanceDetailVO();
		BeanUtils.copyProperties(po, vo);
		vo.setChangeTypeName(DictHolder.getDictItemName("memberBalanceChangeType", vo.getChangeType()));
		return vo;
	}

}
