package com.nft.dataclean.service;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.nft.dataclean.param.DataCleanParam;
import com.nft.log.repo.LoginLogRepo;
import com.nft.log.repo.MemberBalanceChangeLogRepo;
import com.nft.log.repo.OperLogRepo;
import com.nft.sms.repo.SmsSendRecordRepo;

import cn.hutool.core.date.DateUtil;

@Validated
@Service
public class DataCleanService {

	@Autowired
	private LoginLogRepo loginLogRepo;

	@Autowired
	private OperLogRepo operLogRepo;

	@Autowired
	private MemberBalanceChangeLogRepo memberBalanceChangeLogRepo;

	@Autowired
	private SmsSendRecordRepo smsSendRecordRepo;

	@Transactional
	public void clean(@Valid DataCleanParam param) {
		List<String> dataTypes = param.getDataTypes();
		Date startTime = DateUtil.beginOfDay(param.getStartTime()).toJdkDate();
		Date endTime = DateUtil.endOfDay(param.getEndTime()).toJdkDate();
		if (dataTypes.contains("loginLog")) {
			loginLogRepo.dataClean(startTime, endTime);
		}
		if (dataTypes.contains("operLog")) {
			operLogRepo.dataClean(startTime, endTime);
		}
		if (dataTypes.contains("memberBalanceChangeLog")) {
			memberBalanceChangeLogRepo.dataClean(startTime, endTime);
		}
		if (dataTypes.contains("smsSendRecord")) {
			smsSendRecordRepo.dataClean(startTime, endTime);
		}
	}

}
