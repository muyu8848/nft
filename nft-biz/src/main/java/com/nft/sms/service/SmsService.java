package com.nft.sms.service;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.nft.common.vo.PageResult;
import com.nft.constants.Constant;
import com.nft.sms.domain.SmsSendRecord;
import com.nft.sms.param.SmsSendRecordQueryCondParam;
import com.nft.sms.repo.SmsSendRecordRepo;
import com.nft.sms.vo.SmsSendRecordVO;
import com.zengtengpeng.annotation.Lock;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@Service
public class SmsService {

	@Autowired
	private SmsSendRecordRepo smsSendRecordRepo;

	@Transactional(readOnly = true)
	public PageResult<SmsSendRecordVO> findSendRecordByPage(@Valid SmsSendRecordQueryCondParam param) {
		Specification<SmsSendRecord> spec = param.buildSpecification();
		Page<SmsSendRecord> result = smsSendRecordRepo.findAll(spec,
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("createTime"))));
		PageResult<SmsSendRecordVO> pageResult = new PageResult<>(SmsSendRecordVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

	@Lock(keys = "'sendSms' + #id")
	@Transactional
	public void sendSms(String id) {
		SmsSendRecord smsSendRecord = smsSendRecordRepo.getOne(id);
		if (Constant.短信发送状态_发送成功.equals(smsSendRecord.getState())) {
			return;
		}
		try {
			// 这里调短信接口
			String returnCode = "0";
			String returnMsg = "error";
			if ("0".equals(returnCode)) {
				smsSendRecord.success();
			} else {
				smsSendRecord.fail(returnMsg);
			}
		} catch (Exception e) {
			log.error("短信接口异常:" + e.getMessage());
			smsSendRecord.fail("短信接口异常");
		}
		smsSendRecordRepo.save(smsSendRecord);
	}

}
