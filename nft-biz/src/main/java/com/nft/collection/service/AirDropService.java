package com.nft.collection.service;

import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.nft.collection.domain.AirDropRecord;
import com.nft.collection.domain.Collection;
import com.nft.collection.domain.IssuedCollection;
import com.nft.collection.domain.IssuedCollectionActionLog;
import com.nft.collection.domain.MemberHoldCollection;
import com.nft.collection.param.AirDropParam;
import com.nft.collection.repo.AirDropRecordRepo;
import com.nft.collection.repo.CollectionRepo;
import com.nft.collection.repo.IssuedCollectionActionLogRepo;
import com.nft.collection.repo.IssuedCollectionRepo;
import com.nft.collection.repo.MemberHoldCollectionRepo;
import com.nft.common.exception.BizException;
import com.nft.common.utils.ThreadPoolUtils;
import com.nft.constants.Constant;
import com.nft.member.domain.Member;
import com.nft.member.repo.MemberRepo;

@Validated
@Service
public class AirDropService {
	
	@Autowired
	private RedissonClient redissonClient;

	@Autowired
	private CollectionRepo collectionRepo;

	@Autowired
	private IssuedCollectionRepo issuedCollectionRepo;

	@Autowired
	private IssuedCollectionActionLogRepo issuedCollectionActionLogRepo;

	@Autowired
	private MemberHoldCollectionRepo memberHoldCollectionRepo;

	@Autowired
	private AirDropRecordRepo airDropRecordRepo;
	
	@Autowired
	private MemberRepo memberRepo;

	@Transactional
	public void airDrop(@Valid AirDropParam param) {
		Member member = memberRepo.getOne(param.getMemberId());
		member.validBasicRisk();
		Collection collection = collectionRepo.getOne(param.getCollectionId());
		if (collection.getStock() <= 0) {
			throw new BizException("库存不足了");
		}

		collection.setStock(collection.getStock() - 1);
		collectionRepo.save(collection);

		IssuedCollection issuedCollection = collection.issue();
		issuedCollectionRepo.save(issuedCollection);

		issuedCollectionActionLogRepo
				.save(IssuedCollectionActionLog.buildWithAirDrop(param.getMemberId(), issuedCollection.getId()));

		MemberHoldCollection memberHoldCollection = issuedCollection.firstIssueToMember(param.getMemberId(),
				collection.getPrice(), Constant.藏品获取方式_空投);
		memberHoldCollectionRepo.save(memberHoldCollection);

		airDropRecordRepo
				.save(AirDropRecord.build(param.getMemberId(), param.getCollectionId(), issuedCollection.getId()));
		
		ThreadPoolUtils.getSyncChainPool().schedule(() -> {
			redissonClient.getTopic(Constant.上链_铸造艺术品).publish(memberHoldCollection.getId());
		}, 1, TimeUnit.SECONDS);
	}

}
