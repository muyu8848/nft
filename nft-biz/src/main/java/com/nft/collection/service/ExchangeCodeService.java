package com.nft.collection.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.nft.collection.domain.Collection;
import com.nft.collection.domain.ExchangeCode;
import com.nft.collection.domain.IssuedCollection;
import com.nft.collection.domain.IssuedCollectionActionLog;
import com.nft.collection.domain.MemberHoldCollection;
import com.nft.collection.param.ExchangeCodeQueryCondParam;
import com.nft.collection.param.ExchangeParam;
import com.nft.collection.param.GenerateExchangeCodeParam;
import com.nft.collection.repo.CollectionRepo;
import com.nft.collection.repo.ExchangeCodeRepo;
import com.nft.collection.repo.IssuedCollectionActionLogRepo;
import com.nft.collection.repo.IssuedCollectionRepo;
import com.nft.collection.repo.MemberHoldCollectionRepo;
import com.nft.collection.vo.ExchangeCodeVO;
import com.nft.collection.vo.ExchangeResultVO;
import com.nft.common.exception.BizException;
import com.nft.common.utils.ThreadPoolUtils;
import com.nft.constants.Constant;
import com.nft.member.domain.Member;
import com.nft.member.repo.MemberRepo;

@Validated
@Service
public class ExchangeCodeService {

	@Autowired
	private RedissonClient redissonClient;

	@Autowired
	private ExchangeCodeRepo exchangeCodeRepo;

	@Autowired
	private CollectionRepo collectionRepo;

	@Autowired
	private IssuedCollectionRepo issuedCollectionRepo;

	@Autowired
	private IssuedCollectionActionLogRepo issuedCollectionActionLogRepo;

	@Autowired
	private MemberHoldCollectionRepo memberHoldCollectionRepo;
	
	@Autowired
	private MemberRepo memberRepo;

	@Transactional(readOnly = true)
	public List<ExchangeCodeVO> findExchangeCode(String collectionId) {
		ExchangeCodeQueryCondParam param = new ExchangeCodeQueryCondParam();
		param.setCollectionId(collectionId);
		List<ExchangeCode> result = exchangeCodeRepo.findAll(param.buildSpecification(),
				Sort.by(Sort.Order.desc("createTime")));
		return ExchangeCodeVO.convertFor(result);
	}

	@Transactional
	public void generateExchangeCode(@Valid GenerateExchangeCodeParam param) {
		Date now = new Date();
		List<ExchangeCode> exchangeCodes = new ArrayList<ExchangeCode>();
		for (int i = 0; i < param.getQuantity(); i++) {
			exchangeCodes.add(ExchangeCode.build(param.getCollectionId(), now));
		}
		exchangeCodeRepo.saveAll(exchangeCodes);
	}

	@Transactional
	public ExchangeResultVO exchange(ExchangeParam param) {
		Member member = memberRepo.getOne(param.getMemberId());
		member.validBasicRisk();
		ExchangeCode exchangeCode = exchangeCodeRepo.findTopByCodeAndStateAndDeletedFlagFalse(param.getCode(),
				Constant.兑换码状态_未兑换);
		if (exchangeCode == null) {
			throw new BizException("兑换码无效");
		}
		Collection collection = exchangeCode.getCollection();
		if (collection.getStock() <= 0) {
			throw new BizException("库存不足了");
		}

		collection.setStock(collection.getStock() - 1);
		collectionRepo.save(collection);

		IssuedCollection issuedCollection = collection.issue();
		issuedCollectionRepo.save(issuedCollection);

		issuedCollectionActionLogRepo
				.save(IssuedCollectionActionLog.buildWithExchangeCode(param.getMemberId(), issuedCollection.getId()));

		MemberHoldCollection memberHoldCollection = issuedCollection.firstIssueToMember(param.getMemberId(),
				collection.getPrice(), Constant.藏品获取方式_兑换码);
		memberHoldCollectionRepo.save(memberHoldCollection);

		exchangeCode.used(issuedCollection.getId(), param.getMemberId());
		exchangeCodeRepo.save(exchangeCode);

		ThreadPoolUtils.getSyncChainPool().schedule(() -> {
			redissonClient.getTopic(Constant.上链_铸造艺术品).publish(memberHoldCollection.getId());
		}, 1, TimeUnit.SECONDS);

		return ExchangeResultVO.build(collection.getName(), collection.getCover());
	}

}
