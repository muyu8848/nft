package com.nft.collection.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.nft.collection.domain.Collection;
import com.nft.collection.domain.CollectionStory;
import com.nft.collection.domain.Creator;
import com.nft.collection.domain.IssuedCollection;
import com.nft.collection.domain.IssuedCollectionActionLog;
import com.nft.collection.domain.MemberHoldCollection;
import com.nft.collection.domain.MemberResaleCollection;
import com.nft.collection.domain.MysteryBoxCommodity;
import com.nft.collection.param.AddCollectionParam;
import com.nft.collection.param.CollectionQueryCondParam;
import com.nft.collection.param.MemberHoldCollectionQueryCondParam;
import com.nft.collection.param.MysteryBoxCommodityParam;
import com.nft.collection.param.ResaleCollectionQueryCondParam;
import com.nft.collection.param.UpdateCollectionStoryParam;
import com.nft.collection.param.UpdateMysteryBoxCommodityParam;
import com.nft.collection.repo.CollectionRepo;
import com.nft.collection.repo.CollectionStoryRepo;
import com.nft.collection.repo.CreatorRepo;
import com.nft.collection.repo.IssuedCollectionActionLogRepo;
import com.nft.collection.repo.IssuedCollectionRepo;
import com.nft.collection.repo.MemberHoldCollectionRepo;
import com.nft.collection.repo.MemberResaleCollectionRepo;
import com.nft.collection.repo.MysteryBoxCommodityRepo;
import com.nft.collection.vo.CollectionStatisticDataVO;
import com.nft.collection.vo.CollectionVO;
import com.nft.collection.vo.ForSaleCollectionVO;
import com.nft.collection.vo.GroupByDateCollectionVO;
import com.nft.collection.vo.GroupByTimeCollectionVO;
import com.nft.collection.vo.IssuedCollectionActionLogVO;
import com.nft.collection.vo.IssuedCollectionVO;
import com.nft.collection.vo.LatestCollectionDetailVO;
import com.nft.collection.vo.LatestCollectionVO;
import com.nft.collection.vo.MemberHoldCollectionVO;
import com.nft.collection.vo.MemberResaleCollectionVO;
import com.nft.collection.vo.MyHoldCollectionDetailVO;
import com.nft.collection.vo.MyHoldCollectionVO;
import com.nft.collection.vo.MyHoldVO;
import com.nft.collection.vo.MyResaleCollectionDetailVO;
import com.nft.collection.vo.MyResaleCollectionVO;
import com.nft.collection.vo.MySoldCollectionVO;
import com.nft.collection.vo.MysteryBoxCommodityVO;
import com.nft.collection.vo.PublishedBrandDictVO;
import com.nft.collection.vo.PublishedCollectionDictVO;
import com.nft.collection.vo.ResaleCollectionDetailVO;
import com.nft.collection.vo.ResaleCollectionVO;
import com.nft.common.exception.BizException;
import com.nft.common.utils.IdUtils;
import com.nft.common.utils.ThreadPoolUtils;
import com.nft.common.vo.PageResult;
import com.nft.constants.Constant;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;

@Validated
@Service
public class CollectionService {

	@Autowired
	private RedissonClient redissonClient;

	@Autowired
	private CollectionRepo collectionRepo;

	@Autowired
	private CollectionStoryRepo collectionStoryRepo;

	@Autowired
	private CreatorRepo creatorRepo;

	@Autowired
	private MemberHoldCollectionRepo memberHoldCollectionRepo;

	@Autowired
	private MemberResaleCollectionRepo memberResaleCollectionRepo;

	@Autowired
	private IssuedCollectionActionLogRepo issuedCollectionActionLogRepo;

	@Autowired
	private MysteryBoxCommodityRepo mysteryBoxCommodityRepo;

	@Autowired
	private IssuedCollectionRepo issuedCollectionRepo;

	@Transactional(readOnly = true)
	public CollectionStatisticDataVO getCollectionStatisticData() {
		List<Collection> result = collectionRepo.findAll(new CollectionQueryCondParam().buildSpecification(),
				Sort.by(Sort.Order.desc("createTime")));
		CollectionStatisticDataVO vo = new CollectionStatisticDataVO();
		for (Collection data : result) {
			if (Constant.商品类型_藏品.equals(data.getCommodityType())) {
				vo.setCollectionCount(vo.getCollectionCount() + 1);
			} else if (Constant.商品类型_盲盒.equals(data.getCommodityType())) {
				vo.setMysteryBoxCount(vo.getMysteryBoxCount() + 1);
			}
		}
		return vo;
	}

	@Transactional(readOnly = true)
	public List<PublishedBrandDictVO> findPublishedBrandAndCollectionDict() {
		List<Creator> creators = creatorRepo.findByDeletedFlagFalseOrderByCreateTimeDesc();
		List<Collection> collections = collectionRepo.findByDeletedFlagFalseOrderByCreateTimeDesc();
		List<PublishedBrandDictVO> vos = PublishedBrandDictVO.convertFor(creators);
		for (PublishedBrandDictVO vo : vos) {
			for (Collection collection : collections) {
				if (vo.getId().equals(collection.getCreatorId())) {
					vo.getCollections().add(PublishedCollectionDictVO.convertFor(collection));
				}
			}
		}
		return vos;
	}

	@Transactional(readOnly = true)
	public List<IssuedCollectionVO> findIssuedCollection(@NotBlank String collectionId) {
		List<IssuedCollection> artworks = issuedCollectionRepo
				.findByCollectionIdAndDeletedFlagFalseOrderByCollectionSerialNumberDesc(collectionId);
		return IssuedCollectionVO.convertFor(artworks);
	}

	@Transactional(readOnly = true)
	public List<IssuedCollectionActionLogVO> findIssuedCollectionActionLog(@NotBlank String issuedCollectionId) {
		List<IssuedCollectionActionLog> logs = issuedCollectionActionLogRepo
				.findByIssuedCollectionIdOrderByActionTimeDesc(issuedCollectionId);
		return IssuedCollectionActionLogVO.convertFor(logs);
	}

	@Transactional(readOnly = true)
	public PageResult<MemberHoldCollectionVO> findMemberHoldCollectionByPage(
			@Valid MemberHoldCollectionQueryCondParam param) {
		Page<MemberHoldCollection> result = memberHoldCollectionRepo.findAll(param.buildSpecification(),
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("holdTime"))));
		PageResult<MemberHoldCollectionVO> pageResult = new PageResult<>(
				MemberHoldCollectionVO.convertFor(result.getContent()), param.getPageNum(), param.getPageSize(),
				result.getTotalElements());
		return pageResult;
	}

	@Transactional(readOnly = true)
	public ResaleCollectionDetailVO findResaleCollectionDetail(@NotBlank String id) {
		MemberResaleCollection memberResaleCollection = memberResaleCollectionRepo.getOne(id);
		return ResaleCollectionDetailVO.convertFor(memberResaleCollection);
	}

	@Transactional(readOnly = true)
	public MyResaleCollectionDetailVO findMyResaleCollectionDetail(@NotBlank String id, @NotBlank String memberId) {
		MemberResaleCollection memberResaleCollection = memberResaleCollectionRepo.getOne(id);
		if (!memberId.equals(memberResaleCollection.getMemberId())) {
			throw new BizException("无权操作");
		}
		return MyResaleCollectionDetailVO.convertFor(memberResaleCollection);
	}

	@Transactional(readOnly = true)
	public MyHoldCollectionDetailVO findMyHoldCollectionDetail(@NotBlank String id, @NotBlank String memberId) {
		MemberHoldCollection memberHoldCollection = memberHoldCollectionRepo.getOne(id);
		if (!memberId.equals(memberHoldCollection.getMemberId())) {
			throw new BizException("无权操作");
		}
		return MyHoldCollectionDetailVO.convertFor(memberHoldCollection);
	}

	@Transactional(readOnly = true)
	public PageResult<MySoldCollectionVO> findMySoldCollectionByPage(@Valid ResaleCollectionQueryCondParam param) {
		param.setState(Constant.转售的藏品状态_已卖出);
		Page<MemberResaleCollection> result = memberResaleCollectionRepo.findAll(param.buildSpecification(),
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("soldTime"))));
		PageResult<MySoldCollectionVO> pageResult = new PageResult<>(MySoldCollectionVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

	@Transactional(readOnly = true)
	public PageResult<MemberResaleCollectionVO> findMemberResaleCollectionByPage(
			@Valid ResaleCollectionQueryCondParam param) {
		Page<MemberResaleCollection> result = memberResaleCollectionRepo.findAll(param.buildSpecification(),
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("resaleTime"))));
		PageResult<MemberResaleCollectionVO> pageResult = new PageResult<>(
				MemberResaleCollectionVO.convertFor(result.getContent()), param.getPageNum(), param.getPageSize(),
				result.getTotalElements());
		return pageResult;
	}

	@Transactional(readOnly = true)
	public PageResult<ResaleCollectionVO> findResaleCollectionByPage(@Valid ResaleCollectionQueryCondParam param) {
		param.setState(Constant.转售的藏品状态_已发布);
		Page<MemberResaleCollection> result = memberResaleCollectionRepo.findAll(param.buildSpecification(),
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(),
						Sort.by(Direction.fromString(param.getDirection()), param.getPropertie())));
		PageResult<ResaleCollectionVO> pageResult = new PageResult<>(ResaleCollectionVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

	@Transactional(readOnly = true)
	public PageResult<MyResaleCollectionVO> findMyResaleCollectionByPage(@Valid ResaleCollectionQueryCondParam param) {
		param.setState(Constant.转售的藏品状态_已发布);
		Page<MemberResaleCollection> result = memberResaleCollectionRepo.findAll(param.buildSpecification(),
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("resaleTime"))));
		PageResult<MyResaleCollectionVO> pageResult = new PageResult<>(
				MyResaleCollectionVO.convertFor(result.getContent()), param.getPageNum(), param.getPageSize(),
				result.getTotalElements());
		return pageResult;
	}

	@Transactional(readOnly = true)
	public List<MyHoldVO> findAllMyHold(String memberId) {
		MemberHoldCollectionQueryCondParam param = new MemberHoldCollectionQueryCondParam();
		param.setState(Constant.持有藏品状态_持有中);
		param.setMemberId(memberId);
		List<MemberHoldCollection> result = memberHoldCollectionRepo.findAll(param.buildSpecification(),
				Sort.by(Sort.Order.desc("holdTime")));
		return MyHoldVO.convertFor(result);
	}

	@Transactional(readOnly = true)
	public PageResult<MyHoldCollectionVO> findMyHoldMysteryBoxByPage(@Valid MemberHoldCollectionQueryCondParam param) {
		param.setState(Constant.持有藏品状态_持有中);
		param.setCommodityType(Constant.商品类型_盲盒);
		Page<MemberHoldCollection> result = memberHoldCollectionRepo.findAll(param.buildSpecification(),
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("holdTime"))));
		PageResult<MyHoldCollectionVO> pageResult = new PageResult<>(MyHoldCollectionVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

	@Transactional(readOnly = true)
	public PageResult<MyHoldCollectionVO> findMyHoldCollectionByPage(@Valid MemberHoldCollectionQueryCondParam param) {
		param.setState(Constant.持有藏品状态_持有中);
		param.setCommodityType(Constant.商品类型_藏品);
		Page<MemberHoldCollection> result = memberHoldCollectionRepo.findAll(param.buildSpecification(),
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("holdTime"))));
		PageResult<MyHoldCollectionVO> pageResult = new PageResult<>(MyHoldCollectionVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

	@Transactional
	public void delCollection(@NotBlank String id) {
		Collection collection = collectionRepo.getOne(id);
		collection.deleted();
		collectionRepo.save(collection);
	}

	@Transactional(readOnly = true)
	public LatestCollectionDetailVO findLatestCollectionDetailById(@NotBlank String id) {
		Collection collection = collectionRepo.getOne(id);
		return LatestCollectionDetailVO.convertFor(collection);
	}

	@Transactional(readOnly = true)
	public CollectionVO findCollectionById(@NotBlank String id) {
		Collection collection = collectionRepo.getOne(id);
		return CollectionVO.convertFor(collection);
	}

	@Transactional(readOnly = true)
	public List<GroupByDateCollectionVO> findForSaleCollection() {
		CollectionQueryCondParam param = new CollectionQueryCondParam();
		param.setSaleTimeStart(new Date());
		param.setSaleTimeEnd(DateUtil.offset(new Date(), DateField.DAY_OF_YEAR, 10).toJdkDate());
		List<Collection> result = collectionRepo.findAll(param.buildSpecification(),
				Sort.by(Sort.Order.asc("saleTime")));
		Map<String, List<Collection>> dateMap = new LinkedHashMap<String, List<Collection>>();
		for (Collection collection : result) {
			String date = DateUtil.format(collection.getSaleTime(), "MM月dd日");
			if (dateMap.get(date) == null) {
				dateMap.put(date, new ArrayList<>());
			}
			dateMap.get(date).add(collection);
		}
		List<GroupByDateCollectionVO> vos = new ArrayList<>();
		for (Entry<String, List<Collection>> entry : dateMap.entrySet()) {
			Map<String, List<Collection>> timeMap = new LinkedHashMap<String, List<Collection>>();
			for (Collection collection : entry.getValue()) {
				String time = DateUtil.format(collection.getSaleTime(), "HH:mm");
				if (timeMap.get(time) == null) {
					timeMap.put(time, new ArrayList<>());
				}
				timeMap.get(time).add(collection);
			}
			GroupByDateCollectionVO groupByDateVO = new GroupByDateCollectionVO();
			for (Entry<String, List<Collection>> time : timeMap.entrySet()) {
				GroupByTimeCollectionVO groupByTimeVO = new GroupByTimeCollectionVO();
				groupByTimeVO.setTime(time.getKey());
				groupByTimeVO.setCollections(ForSaleCollectionVO.convertFor(time.getValue()));
				groupByDateVO.getTimeCollections().add(groupByTimeVO);
			}
			groupByDateVO.setDate(entry.getKey());
			vos.add(groupByDateVO);
		}
		return vos;
	}

	@Transactional(readOnly = true)
	public PageResult<LatestCollectionVO> findLatestMysteryBoxByPage(@Valid CollectionQueryCondParam param) {
		param.setCommodityType(Constant.商品类型_盲盒);
		param.setSaleTimeEnd(DateUtil.offset(new Date(), DateField.DAY_OF_YEAR, 2).toJdkDate());
		Page<Collection> result = collectionRepo.findAll(param.buildSpecification(),
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("saleTime"))));
		PageResult<LatestCollectionVO> pageResult = new PageResult<>(LatestCollectionVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

	@Transactional(readOnly = true)
	public PageResult<LatestCollectionVO> findLatestCollectionByPage(@Valid CollectionQueryCondParam param) {
		param.setCommodityType(Constant.商品类型_藏品);
		param.setSaleTimeEnd(DateUtil.offset(new Date(), DateField.DAY_OF_YEAR, 2).toJdkDate());
		Page<Collection> result = collectionRepo.findAll(param.buildSpecification(),
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("saleTime"))));
		PageResult<LatestCollectionVO> pageResult = new PageResult<>(LatestCollectionVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

	@Transactional(readOnly = true)
	public List<CollectionVO> findAllCollection() {
		CollectionQueryCondParam param = new CollectionQueryCondParam();
		param.setCommodityType(Constant.商品类型_藏品);
		List<Collection> result = collectionRepo.findAll(param.buildSpecification(),
				Sort.by(Sort.Order.desc("createTime")));
		return CollectionVO.convertFor(result);
	}

	@Transactional(readOnly = true)
	public PageResult<CollectionVO> findCollectionByPage(@Valid CollectionQueryCondParam param) {
		Page<Collection> result = collectionRepo.findAll(param.buildSpecification(),
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("createTime"))));
		PageResult<CollectionVO> pageResult = new PageResult<>(CollectionVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

	@Transactional(readOnly = true)
	public List<MysteryBoxCommodityVO> findMysteryBoxCommodity(@NotBlank String collectionId) {
		List<MysteryBoxCommodity> mysteryBoxCommoditys = mysteryBoxCommodityRepo
				.findByCollectionIdOrderByProbabilityAsc(collectionId);
		return MysteryBoxCommodityVO.convertFor(mysteryBoxCommoditys);
	}

	@Transactional
	public void updateMysteryBoxCommodity(@Valid UpdateMysteryBoxCommodityParam param) {
		List<MysteryBoxCommodity> oldSubCommoditys = mysteryBoxCommodityRepo
				.findByCollectionIdOrderByProbabilityAsc(param.getCollectionId());
		mysteryBoxCommodityRepo.deleteAll(oldSubCommoditys);

		for (MysteryBoxCommodityParam subCommodityParam : param.getSubCommoditys()) {
			MysteryBoxCommodity subCommodity = new MysteryBoxCommodity();
			subCommodity.setId(IdUtils.getId());
			subCommodity.setCommodityId(subCommodityParam.getCommodityId());
			subCommodity.setProbability(subCommodityParam.getProbability());
			subCommodity.setCollectionId(param.getCollectionId());
			mysteryBoxCommodityRepo.save(subCommodity);
		}
	}

	@Transactional
	public void updateCollectionStory(@Valid UpdateCollectionStoryParam param) {
		List<CollectionStory> oldCollectionStorys = collectionStoryRepo
				.findByCollectionIdOrderByOrderNo(param.getCollectionId());
		collectionStoryRepo.deleteAll(oldCollectionStorys);

		double orderNo = 1;
		for (String picLink : param.getPicLinks()) {
			CollectionStory collectionStory = new CollectionStory();
			collectionStory.setId(IdUtils.getId());
			collectionStory.setCollectionId(param.getCollectionId());
			collectionStory.setPicLink(picLink);
			collectionStory.setOrderNo(orderNo);
			collectionStoryRepo.save(collectionStory);
			orderNo++;
		}
	}

	@Transactional
	public void addCollection(@Valid AddCollectionParam param) {
		Collection collection = param.convertToPo();
		collectionRepo.save(collection);

		ThreadPoolUtils.getSyncChainPool().schedule(() -> {
			redissonClient.getTopic(Constant.上链_创建艺术品).publish(collection.getId());
		}, 1, TimeUnit.SECONDS);
	}
}
