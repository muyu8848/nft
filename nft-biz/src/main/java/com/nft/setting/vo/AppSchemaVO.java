package com.nft.setting.vo;

import org.springframework.beans.BeanUtils;

import com.nft.setting.domain.SystemSetting;

import lombok.Data;

@Data
public class AppSchemaVO {

	private String appUrl;

	private Double appVersion;

	private String appSchema;

	public static AppSchemaVO convertFor(SystemSetting setting) {
		AppSchemaVO vo = new AppSchemaVO();
		if (setting != null) {
			BeanUtils.copyProperties(setting, vo);
		}
		return vo;
	}

}
