package com.nft.admin.sms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nft.common.vo.PageResult;
import com.nft.common.vo.Result;
import com.nft.sms.param.SmsSendRecordQueryCondParam;
import com.nft.sms.service.SmsService;
import com.nft.sms.vo.SmsSendRecordVO;

@RestController
@RequestMapping("/sms")
public class SmsController {

	@Autowired
	private SmsService smsService;

	@GetMapping("/findSendRecordByPage")
	public Result<PageResult<SmsSendRecordVO>> findSendRecordByPage(SmsSendRecordQueryCondParam param) {
		return Result.success(smsService.findSendRecordByPage(param));
	}

}
