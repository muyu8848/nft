package com.nft;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nft.chain.service.ChainService;
import com.nft.collection.service.TransactionService;
import com.nft.constants.Constant;
import com.nft.sms.service.SmsService;
import com.zengtengpeng.annotation.MQListener;

@Component
public class RedisMqListener {

	@Autowired
	private SmsService smsService;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private ChainService chainService;

	@MQListener(name = Constant.上链_同步交易HASH)
	public void syncTransactionHash(String topicName, String value) {
		chainService.syncTransactionHash(value);
	}

	@MQListener(name = Constant.上链_同步唯一标识)
	public void syncUniqueId(String topicName, String value) {
		chainService.syncUniqueId(value);
	}

	@MQListener(name = Constant.上链_销毁艺术品)
	public void destroyArtwork(String topicName, String value) {
		chainService.destroyArtwork(value);
	}

	@MQListener(name = Constant.上链_转让艺术品)
	public void transferArtwork(String topicName, String value) {
		chainService.transferArtwork(value);
	}

	@MQListener(name = Constant.上链_二级市场购买艺术品)
	public void marketBuyArtwork(String topicName, String value) {
		chainService.marketBuyArtwork(value);
	}

	@MQListener(name = Constant.上链_铸造艺术品)
	public void mintArtwork(String topicName, String value) {
		chainService.mintArtwork(value);
	}

	@MQListener(name = Constant.上链_同步艺术品HASH)
	public void syncArtworkHash(String topicName, String value) {
		chainService.syncArtworkHash(value);
	}

	@MQListener(name = Constant.上链_创建艺术品)
	public void chainAddArtwork(String topicName, String value) {
		chainService.chainAddArtwork(value);
	}

	@MQListener(name = Constant.创建区块链地址)
	public void createBlockChainAddr(String topicName, String value) {
		chainService.createBlockChainAddr(value);
	}

	@MQListener(name = Constant.支付订单超时取消)
	public void buyerPaidConfirm(String topicName, String value) {
		transactionService.cancelPay(value);
	}

	@MQListener(name = Constant.发送短信)
	public void sendSms(String topicName, String value) {
		smsService.sendSms(value);
	}

}
