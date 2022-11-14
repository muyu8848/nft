package com.nft.collection.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nft.collection.domain.ComposeRecord;
import com.nft.collection.domain.IssuedCollection;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;

@Data
public class ComposeRecordVO {

	private String id;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date composeTime;

	private String memberMobile;

	private String memberBlockChainAddr;

	private Integer collectionSerialNumber;

	public static List<ComposeRecordVO> convertFor(List<ComposeRecord> pos) {
		if (CollectionUtil.isEmpty(pos)) {
			return new ArrayList<>();
		}
		List<ComposeRecordVO> vos = new ArrayList<>();
		for (ComposeRecord po : pos) {
			vos.add(convertFor(po));
		}
		return vos;
	}

	public static ComposeRecordVO convertFor(ComposeRecord po) {
		if (po == null) {
			return null;
		}
		ComposeRecordVO vo = new ComposeRecordVO();
		BeanUtils.copyProperties(po, vo);
		if (po.getMember() != null) {
			vo.setMemberMobile(po.getMember().getMobile());
			vo.setMemberBlockChainAddr(po.getMember().getBlockChainAddr());
		}
		IssuedCollection issuedCollection = po.getIssuedCollection();
		if (issuedCollection != null) {
			vo.setCollectionSerialNumber(issuedCollection.getCollectionSerialNumber());
		}
		return vo;
	}

}
