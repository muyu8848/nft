package com.nft.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nft.collection.service.TransactionService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PayOrderTimeoutCancelTask {

	@Autowired
	private TransactionService transactionService;

	@Scheduled(fixedRate = 12000)
	public void execute() {
		try {
			transactionService.payOrderTimeoutCancel();
		} catch (Exception e) {
			log.error("支付订单超时定时任务", e);
		}
	}

}
