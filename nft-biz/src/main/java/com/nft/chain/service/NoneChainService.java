package com.nft.chain.service;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.nft.collection.domain.Collection;
import com.nft.collection.domain.IssuedCollection;
import com.nft.collection.domain.MemberHoldCollection;
import com.nft.collection.repo.CollectionRepo;
import com.nft.collection.repo.IssuedCollectionRepo;
import com.nft.collection.repo.MemberHoldCollectionRepo;
import com.nft.common.utils.ThreadPoolUtils;
import com.nft.constants.Constant;
import com.nft.member.domain.Member;
import com.nft.member.repo.MemberRepo;
import com.zengtengpeng.annotation.Lock;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;

@Validated
@Service
public class NoneChainService implements ChainAbstractService {

	@Autowired
	private RedissonClient redissonClient;

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	private CollectionRepo collectionRepo;

	@Autowired
	private IssuedCollectionRepo issuedCollectionRepo;

	@Autowired
	private MemberHoldCollectionRepo memberHoldCollectionRepo;

	@Autowired
	private MemberRepo memberRepo;

	@Override
	@Lock(keys = "'syncTransactionHash' + #id")
	@Transactional
	public String syncTransactionHash(String id) {
		MemberHoldCollection memberHoldCollection = memberHoldCollectionRepo.getOne(id);
		if (StrUtil.isNotBlank(memberHoldCollection.getTransactionHash())) {
			return "已上链";
		}
		String txHash = IdUtil.fastSimpleUUID();

		memberHoldCollection.syncChain(txHash);
		memberHoldCollectionRepo.save(memberHoldCollection);
		return "已上链";
	}

	@Override
	@Lock(keys = "'syncUniqueId' + #id")
	@Transactional
	public String syncUniqueId(String id) {
		MemberHoldCollection memberHoldCollection = memberHoldCollectionRepo.getOne(id);
		IssuedCollection issuedCollection = memberHoldCollection.getIssuedCollection();
		if (StrUtil.isNotBlank(issuedCollection.getUniqueId())) {
			return "已上链";
		}

		String nftId = IdUtil.fastSimpleUUID();
		String txHash = IdUtil.fastSimpleUUID();

		issuedCollection.syncChain(nftId);
		issuedCollectionRepo.save(issuedCollection);

		memberHoldCollection.syncChain(txHash);
		memberHoldCollectionRepo.save(memberHoldCollection);
		return "已上链";
	}

	@Override
	@Transactional
	public void transferArtwork(String id) {
		chainTransfer(id);
	}

	@Override
	@Transactional
	public void marketBuyArtwork(String id) {
		chainTransfer(id);
	}

	@Override
	@Lock(keys = "'chainTransfer' + #id")
	@Transactional
	public String chainTransfer(String id) {
		MemberHoldCollection toHoldCollection = memberHoldCollectionRepo.getOne(id);
		if (StrUtil.isNotBlank(toHoldCollection.getTransactionHash())) {
			return "已上链";
		}
		String limitGet = redisTemplate.opsForValue().get(Constant.限制 + Constant.上链_转让艺术品 + id);
		if (StrUtil.isNotBlank(limitGet)) {
			return syncTransactionHash(id);
		}

		redisTemplate.opsForValue().set(Constant.限制 + Constant.上链_转让艺术品 + id, "1", 60, TimeUnit.SECONDS);
		ThreadPoolUtils.getSyncChainPool().schedule(() -> {
			redissonClient.getTopic(Constant.上链_同步交易HASH).publish(toHoldCollection.getId());
		}, 35, TimeUnit.SECONDS);
		return "上链确认中";
	}

	@Override
	@Transactional
	public void destroyArtwork(String id) {
	}

	@Override
	@Lock(keys = "'mintArtwork' + #id")
	@Transactional
	public String mintArtwork(String id) {
		MemberHoldCollection memberHoldCollection = memberHoldCollectionRepo.getOne(id);
		IssuedCollection issuedCollection = memberHoldCollection.getIssuedCollection();
		if (StrUtil.isNotBlank(issuedCollection.getUniqueId())) {
			return "已上链";
		}
		String limitGet = redisTemplate.opsForValue().get(Constant.限制 + Constant.上链_铸造艺术品 + id);
		if (StrUtil.isNotBlank(limitGet)) {
			return syncUniqueId(id);
		}
		redisTemplate.opsForValue().set(Constant.限制 + Constant.上链_铸造艺术品 + id, "1", 60, TimeUnit.SECONDS);
		ThreadPoolUtils.getSyncChainPool().schedule(() -> {
			redissonClient.getTopic(Constant.上链_同步唯一标识).publish(id);
		}, 35, TimeUnit.SECONDS);
		return "上链确认中";
	}

	@Override
	@Lock(keys = "'createBlockChainAddr' + #id")
	@Transactional
	public String createBlockChainAddr(String id) {
		Member member = memberRepo.getOne(id);
		if (StrUtil.isNotBlank(member.getBlockChainAddr())) {
			return "已上链";
		}
		String limitGet = redisTemplate.opsForValue().get(Constant.限制 + Constant.创建区块链地址 + id);
		if (StrUtil.isNotBlank(limitGet)) {
			return "处理中";
		}
		redisTemplate.opsForValue().set(Constant.限制 + Constant.创建区块链地址 + id, "1", 60, TimeUnit.SECONDS);

		String blockChainAddr = IdUtil.fastSimpleUUID();

		member.syncChain(blockChainAddr);
		memberRepo.save(member);
		return "已上链";
	}

	@Override
	@Lock(keys = "'syncArtworkHash' + #id")
	@Transactional
	public String syncArtworkHash(String id) {
		Collection artwork = collectionRepo.getOne(id);
		if (StrUtil.isNotBlank(artwork.getCollectionHash())) {
			return "已上链";
		}
		String txHash = IdUtil.fastSimpleUUID();
		artwork.syncChain(txHash);
		collectionRepo.save(artwork);
		return "已上链";
	}

	@Override
	@Lock(keys = "'chainAddArtwork' + #id")
	@Transactional
	public String chainAddArtwork(String id) {
		Collection artwork = collectionRepo.getOne(id);
		if (StrUtil.isNotBlank(artwork.getCollectionHash())) {
			return "已上链";
		}
		String limitGet = redisTemplate.opsForValue().get(Constant.限制 + Constant.上链_创建艺术品 + id);
		if (StrUtil.isNotBlank(limitGet)) {
			return syncArtworkHash(id);
		}

		redisTemplate.opsForValue().set(Constant.限制 + Constant.上链_创建艺术品 + id, "1", 60, TimeUnit.SECONDS);
		ThreadPoolUtils.getSyncChainPool().schedule(() -> {
			redissonClient.getTopic(Constant.上链_同步艺术品HASH).publish(id);
		}, 35, TimeUnit.SECONDS);
		return "上链确认中";
	}

}
