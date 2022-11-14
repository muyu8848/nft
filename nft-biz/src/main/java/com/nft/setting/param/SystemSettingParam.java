package com.nft.setting.param;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SystemSettingParam {

	@NotBlank
	private String appUrl;

	@NotNull
	@DecimalMin(value = "0", inclusive = true)
	private Double appVersion;

	@NotBlank
	private String appSchema;

	@NotBlank
	private String h5Gateway;

	@NotBlank
	private String localStoragePath;
	
	private String customerServiceUrl;

}
