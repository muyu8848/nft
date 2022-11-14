package com.nft.setting.vo;

import org.springframework.beans.BeanUtils;

import com.nft.setting.domain.SystemSetting;

import lombok.Data;

@Data
public class SystemSettingVO {

	private String appUrl;

	private Double appVersion;

	private String appSchema;

	private String h5Gateway;

	private String localStoragePath;

	private String customerServiceUrl;

	public static SystemSettingVO convertFor(SystemSetting po) {
		SystemSettingVO vo = new SystemSettingVO();
		if (po != null) {
			BeanUtils.copyProperties(po, vo);
		}
		return vo;
	}

}
