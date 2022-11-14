package com.nft.collection.vo;

import cn.hutool.core.util.DesensitizedUtil;
import lombok.Data;

@Data
public class CollectionReceiverInfoVO {

	private String mobile;

	private String blockChainAddr;

	public static CollectionReceiverInfoVO build(String mobile, String blockChainAddr) {
		CollectionReceiverInfoVO vo = new CollectionReceiverInfoVO();
		vo.setMobile(DesensitizedUtil.mobilePhone(mobile));
		vo.setBlockChainAddr(blockChainAddr);
		return vo;
	}

}
