package com.nft.setting.vo;

import org.springframework.beans.BeanUtils;

import com.nft.setting.domain.SystemSetting;

import lombok.Data;

@Data
public class LatestAppInfoVO {

	private String appUrl;

	private Double appVersion;

	public static LatestAppInfoVO convertFor(SystemSetting setting) {
		LatestAppInfoVO vo = new LatestAppInfoVO();
		if (setting != null) {
			BeanUtils.copyProperties(setting, vo);
		}
		return vo;
	}

}
