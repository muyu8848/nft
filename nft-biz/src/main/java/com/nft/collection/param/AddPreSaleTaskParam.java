package com.nft.collection.param;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nft.collection.domain.PreSaleTask;
import com.nft.common.utils.IdUtils;
import com.nft.constants.Constant;

import lombok.Data;

@Data
public class AddPreSaleTaskParam {

	@NotBlank
	private String taskName;

	@NotBlank
	private String collectionId;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	private Date executeTime;
	
	@NotNull
	@DecimalMin(value = "0", inclusive = false)
	private Integer preMinute;

	@Valid
	private List<PreSaleConditionParam> preSaleConditions;

	public PreSaleTask convertToPo() {
		PreSaleTask po = new PreSaleTask();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setCreateTime(new Date());
		po.setState(Constant.优先购任务状态_未执行);
		if (po.getExecuteTime() == null) {
			po.setExecuteTime(new Date());
		}
		return po;
	}

}
