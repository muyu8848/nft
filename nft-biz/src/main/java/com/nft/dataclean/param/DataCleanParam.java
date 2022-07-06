package com.nft.dataclean.param;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class DataCleanParam {

	private List<String> dataTypes;

	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startTime;

	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endTime;

}
