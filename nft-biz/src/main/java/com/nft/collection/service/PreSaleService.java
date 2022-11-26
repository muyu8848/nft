package com.nft.collection.service;

import java.util.Date;
import java.util.List;

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
import com.nft.collection.domain.PreSaleCondition;
import com.nft.collection.domain.PreSaleQualify;
import com.nft.collection.domain.PreSaleTask;
import com.nft.collection.param.AddPreSaleQualifyParam;
import com.nft.collection.param.AddPreSaleTaskParam;
import com.nft.collection.param.PreSaleConditionParam;
import com.nft.collection.param.PreSaleQualifyQueryCondParam;
import com.nft.collection.param.PreSaleTaskQueryCondParam;
import com.nft.collection.repo.CollectionRepo;
import com.nft.collection.repo.PreSaleConditionRepo;
import com.nft.collection.repo.PreSaleQualifyRepo;
import com.nft.collection.repo.PreSaleTaskRepo;
import com.nft.collection.vo.PreSaleQualifyVO;
import com.nft.collection.vo.PreSaleTaskVO;
import com.nft.common.vo.PageResult;
import com.nft.constants.Constant;
import com.nft.member.service.MemberService;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;

@Validated
@Service
public class PreSaleService {

	@Autowired
	private RedissonClient redissonClient;

	@Autowired
	private MemberService memberService;

	@Autowired
	private CollectionRepo collectionRepo;

	@Autowired
	private PreSaleQualifyRepo preSaleQualifyRepo;

	@Autowired
	private PreSaleTaskRepo preSaleTaskRepo;

	@Autowired
	private PreSaleConditionRepo preSaleConditionRepo;

	@Transactional(readOnly = true)
	public PageResult<PreSaleQualifyVO> findPreSaleQualifyByPage(@Valid PreSaleQualifyQueryCondParam param) {
		Page<PreSaleQualify> result = preSaleQualifyRepo.findAll(param.buildSpecification(),
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("grantTime"))));
		PageResult<PreSaleQualifyVO> pageResult = new PageResult<>(PreSaleQualifyVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

	@Transactional(readOnly = true)
	public List<PreSaleTaskVO> findAllPreSaleTask() {
		PreSaleTaskQueryCondParam param = new PreSaleTaskQueryCondParam();
		List<PreSaleTask> result = preSaleTaskRepo.findAll(param.buildSpecification(),
				Sort.by(Sort.Order.desc("createTime")));
		return PreSaleTaskVO.convertFor(result);
	}

	@Transactional(readOnly = true)
	public PageResult<PreSaleTaskVO> findPreSaleTaskByPage(@Valid PreSaleTaskQueryCondParam param) {
		Page<PreSaleTask> result = preSaleTaskRepo.findAll(param.buildSpecification(),
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("createTime"))));
		PageResult<PreSaleTaskVO> pageResult = new PageResult<>(PreSaleTaskVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

	@Transactional
	public void addPreSaleTask(@Valid AddPreSaleTaskParam param) {
		Collection collection = collectionRepo.getOne(param.getCollectionId());
		PreSaleTask preSaleTask = param.convertToPo();
		preSaleTaskRepo.save(preSaleTask);

		double orderNo = 1;
		for (PreSaleConditionParam preSaleConditionParam : param.getPreSaleConditions()) {
			PreSaleCondition preSaleCondition = preSaleConditionParam.convertToPo(preSaleTask.getId(), orderNo);
			preSaleConditionRepo.save(preSaleCondition);

			orderNo++;
		}
	}

	@Transactional
	public void executePreSaleTask() {
		Date now = new Date();
		List<PreSaleTask> preSaleTasks = preSaleTaskRepo.findByStateAndExecuteTimeLessThan(Constant.优先购任务状态_未执行, now);
		for (PreSaleTask preSaleTask : preSaleTasks) {
			redissonClient.getTopic(Constant.执行优先购任务).publish(preSaleTask.getId());
		}
	}

	@Transactional(readOnly = true)
	public Boolean checkHasPreSale(@NotBlank String collectionId, @NotBlank String memberId) {
		Collection collection = collectionRepo.getOne(collectionId);
		Date now = new Date();
		if (now.getTime() >= collection.getSaleTime().getTime()) {
			return false;
		}
		PreSaleQualify preSaleQualify = preSaleQualifyRepo.findTopByMemberIdAndCollectionIdAndStateOrderByPreMinuteDesc(
				memberId, collectionId, Constant.优先购资格状态_未使用);
		if (preSaleQualify == null) {
			return false;
		}
		Date preMinuteAfterTime = DateUtil
				.offset(collection.getSaleTime(), DateField.MINUTE, -preSaleQualify.getPreMinute()).toJdkDate();
		if (DateUtil.compare(now, preMinuteAfterTime) < 0) {
			return false;
		}
		return true;
	}

	@Transactional
	public void addPreSaleQualify(@Valid AddPreSaleQualifyParam param) {
		Collection collection = collectionRepo.getOne(param.getCollectionId());

		PreSaleQualify preSaleQualify = param.convertToPo();
		preSaleQualifyRepo.save(preSaleQualify);
	}

}
