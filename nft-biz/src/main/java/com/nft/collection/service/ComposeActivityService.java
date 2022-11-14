package com.nft.collection.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.nft.collection.domain.Collection;
import com.nft.collection.domain.ComposeActivity;
import com.nft.collection.domain.ComposeMaterial;
import com.nft.collection.domain.ComposeRecord;
import com.nft.collection.domain.IssuedCollection;
import com.nft.collection.domain.IssuedCollectionActionLog;
import com.nft.collection.domain.MemberHoldCollection;
import com.nft.collection.param.AddComposeActivityParam;
import com.nft.collection.param.ComposeActivityQueryCondParam;
import com.nft.collection.param.ComposeMaterialParam;
import com.nft.collection.param.ComposeParam;
import com.nft.collection.param.ComposeRecordQueryCondParam;
import com.nft.collection.param.UpdateComposeMaterialParam;
import com.nft.collection.repo.CollectionRepo;
import com.nft.collection.repo.ComposeActivityRepo;
import com.nft.collection.repo.ComposeMaterialRepo;
import com.nft.collection.repo.ComposeRecordRepo;
import com.nft.collection.repo.IssuedCollectionActionLogRepo;
import com.nft.collection.repo.IssuedCollectionRepo;
import com.nft.collection.repo.MemberHoldCollectionRepo;
import com.nft.collection.vo.ActiveComposeActivityDetailVO;
import com.nft.collection.vo.ActiveComposeActivityVO;
import com.nft.collection.vo.ComposeActivityVO;
import com.nft.collection.vo.ComposeRecordVO;
import com.nft.common.exception.BizException;
import com.nft.common.utils.IdUtils;
import com.nft.common.utils.ThreadPoolUtils;
import com.nft.common.vo.PageResult;
import com.nft.constants.Constant;

import cn.hutool.core.collection.CollUtil;

@Validated
@Service
public class ComposeActivityService {
	
	@Autowired
	private RedissonClient redissonClient;

	@Autowired
	private MemberHoldCollectionRepo memberHoldCollectionRepo;

	@Autowired
	private ComposeActivityRepo composeActivityRepo;

	@Autowired
	private CollectionRepo collectionRepo;

	@Autowired
	private ComposeMaterialRepo composeMaterialRepo;

	@Autowired
	private IssuedCollectionActionLogRepo issuedCollectionActionLogRepo;

	@Autowired
	private IssuedCollectionRepo issuedCollectionRepo;

	@Autowired
	private ComposeRecordRepo composeRecordRepo;

	@Transactional(readOnly = true)
	public List<ComposeRecordVO> findComposeRecord(String composeActivityId, String memberMobile) {
		ComposeRecordQueryCondParam param = new ComposeRecordQueryCondParam();
		param.setComposeActivityId(composeActivityId);
		param.setMemberMobile(memberMobile);
		List<ComposeRecord> result = composeRecordRepo.findAll(param.buildSpecification(),
				Sort.by(Sort.Order.desc("composeTime")));
		return ComposeRecordVO.convertFor(result);
	}

	@Transactional
	public void compose(@Valid ComposeParam param) {
		Date now = new Date();
		ComposeActivity activity = composeActivityRepo.getOne(param.getActivityId());
		if (now.getTime() > activity.getActivityTimeEnd().getTime()) {
			throw new BizException("活动已结束");
		}
		if (activity.getActivityTimeStart().getTime() > now.getTime()) {
			throw new BizException("活动未开始");
		}
		if (activity.getStock() <= 0) {
			throw new BizException("活动太火爆了,请下次提早");
		}
		Collection collection = activity.getCollection();
		if (collection.getStock() <= 0) {
			throw new BizException("库存不足了,请下次提早");
		}
		Integer totalMaterialQuantity = 0;
		Set<ComposeMaterial> materials = activity.getMaterials();
		if (CollUtil.isEmpty(materials)) {
			throw new BizException("活动还没设置原料");
		}
		for (ComposeMaterial material : materials) {
			totalMaterialQuantity += material.getQuantity();
		}
		List<MemberHoldCollection> materialCollections = memberHoldCollectionRepo
				.findByMemberIdAndStateAndIdIn(param.getMemberId(), Constant.持有藏品状态_持有中, param.getHoldCollectionIds());
		if (totalMaterialQuantity - materialCollections.size() != 0) {
			throw new BizException("原料数量对不上");
		}
		for (ComposeMaterial material : materials) {
			Integer selectedQuantity = 0;
			for (MemberHoldCollection materialCollection : materialCollections) {
				if (material.getMaterialId().equals(materialCollection.getCollectionId())) {
					selectedQuantity++;
				}
			}
			if (material.getQuantity() - selectedQuantity != 0) {
				throw new BizException("原料数量对不上");
			}
		}

		activity.setStock(activity.getStock() - 1);
		composeActivityRepo.save(activity);

		for (MemberHoldCollection materialCollection : materialCollections) {
			materialCollection.composeDestroy();
			memberHoldCollectionRepo.save(materialCollection);
			
			IssuedCollection issuedCollection = materialCollection.getIssuedCollection();
			issuedCollection.deleted();
			issuedCollectionRepo.save(issuedCollection);

			issuedCollectionActionLogRepo.save(IssuedCollectionActionLog
					.buildWithComposeDestroy(materialCollection.getMemberId(), materialCollection.getIssuedCollectionId()));
		}

		collection.setStock(collection.getStock() - 1);
		collectionRepo.save(collection);

		IssuedCollection issuedCollection = collection.issue();
		issuedCollectionRepo.save(issuedCollection);

		issuedCollectionActionLogRepo
				.save(IssuedCollectionActionLog.buildWithCompose(param.getMemberId(), issuedCollection.getId()));

		MemberHoldCollection memberHoldCollection = issuedCollection.firstIssueToMember(param.getMemberId(),
				activity.getCollection().getPrice(), Constant.藏品获取方式_合成);
		memberHoldCollectionRepo.save(memberHoldCollection);

		composeRecordRepo
				.save(ComposeRecord.build(param.getMemberId(), param.getActivityId(), issuedCollection.getId()));
		
		ThreadPoolUtils.getSyncChainPool().schedule(() -> {
			for (MemberHoldCollection materialCollection : materialCollections) {
				redissonClient.getTopic(Constant.上链_销毁艺术品).publish(materialCollection.getId());
			}
			redissonClient.getTopic(Constant.上链_铸造艺术品).publish(memberHoldCollection.getId());
		}, 1, TimeUnit.SECONDS);

	}

	@Transactional(readOnly = true)
	public ActiveComposeActivityDetailVO findActiveComposeActivityDetail(@NotBlank String id) {
		ComposeActivity composeActivity = composeActivityRepo.getOne(id);
		return ActiveComposeActivityDetailVO.convertFor(composeActivity);
	}

	@Transactional(readOnly = true)
	public List<ActiveComposeActivityVO> findActiveComposeActivity() {
		Date now = new Date();
		List<ComposeActivity> composeActivitys = composeActivityRepo
				.findByActivityTimeStartLessThanEqualAndActivityTimeEndGreaterThanEqualAndDeletedFlagFalseOrderByActivityTimeEndAsc(
						now, now);
		return ActiveComposeActivityVO.convertFor(composeActivitys);
	}

	@Transactional
	public void del(@NotBlank String id) {
		ComposeActivity composeActivity = composeActivityRepo.getOne(id);
		composeActivity.deleted();
		composeActivityRepo.save(composeActivity);
	}

	@Transactional(readOnly = true)
	public ComposeActivityVO findComposeActivityById(@NotBlank String id) {
		ComposeActivity composeActivity = composeActivityRepo.getOne(id);
		return ComposeActivityVO.convertFor(composeActivity);
	}

	@Transactional(readOnly = true)
	public PageResult<ComposeActivityVO> findComposeActivityByPage(@Valid ComposeActivityQueryCondParam param) {
		Page<ComposeActivity> result = composeActivityRepo.findAll(param.buildSpecification(),
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("createTime"))));
		PageResult<ComposeActivityVO> pageResult = new PageResult<>(ComposeActivityVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

	@Transactional
	public void updateComposeMaterial(@Valid UpdateComposeMaterialParam param) {
		ComposeActivity composeActivity = composeActivityRepo.getOne(param.getActivityId());
		Map<String, String> materialIdMap = new HashMap<>();
		for (ComposeMaterialParam materialParam : param.getMaterials()) {
			if (materialParam.getMaterialId().equals(composeActivity.getCollectionId())) {
				throw new BizException("原料不能跟合成的藏品相同");
			}
			if (materialIdMap.get(materialParam.getMaterialId()) != null) {
				throw new BizException("原料不能重复");
			}
			materialIdMap.put(materialParam.getMaterialId(), materialParam.getMaterialId());
		}
		List<ComposeMaterial> oldMaterials = composeMaterialRepo.findByActivityId(composeActivity.getId());
		composeMaterialRepo.deleteAll(oldMaterials);

		for (ComposeMaterialParam materialParam : param.getMaterials()) {
			ComposeMaterial material = new ComposeMaterial();
			material.setId(IdUtils.getId());
			material.setMaterialId(materialParam.getMaterialId());
			material.setActivityId(param.getActivityId());
			material.setQuantity(materialParam.getQuantity());
			composeMaterialRepo.save(material);
		}
	}

	@Transactional
	public void addComposeActivity(@Valid AddComposeActivityParam param) {
		if (param.getActivityTimeStart().getTime() > param.getActivityTimeEnd().getTime()) {
			throw new BizException("活动时间范围不正确");
		}
		ComposeActivity composeActivity = param.convertToPo();
		composeActivityRepo.save(composeActivity);
	}

}
