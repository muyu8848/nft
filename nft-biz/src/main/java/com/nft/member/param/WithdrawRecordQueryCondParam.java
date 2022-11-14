package com.nft.member.param;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.nft.common.param.PageParam;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class WithdrawRecordQueryCondParam extends PageParam {
	
	private String orderNo;
	
	private String memberId;
	
	private String state;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date submitTimeStart;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date submitTimeEnd;

}
