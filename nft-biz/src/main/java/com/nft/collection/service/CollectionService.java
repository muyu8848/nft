package com.nft.collection.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.nft.collection.domain.Collection;
import com.nft.collection.domain.CollectionGiveRecord;
import com.nft.collection.domain.CollectionStory;
import com.nft.collection.domain.Creator;
import com.nft.collection.domain.MemberHoldCollection;
import com.nft.collection.domain.PayOrder;
import com.nft.collection.param.AddCollectionParam;
import com.nft.collection.param.AddOrUpdateCreatorParam;
import com.nft.collection.param.CancelPayParam;
import com.nft.collection.param.CollectionCancelResaleParam;
import com.nft.collection.param.CollectionGiveParam;
import com.nft.collection.param.CollectionGiveRecordQueryCondParam;
import com.nft.collection.param.CollectionQueryCondParam;
import com.nft.collection.param.CollectionResaleParam;
import com.nft.collection.param.ConfirmPayParam;
import com.nft.collection.param.CreatorQueryCondParam;
import com.nft.collection.param.LatestCollectionCreateOrderParam;
import com.nft.collection.param.MemberHoldCollectionQueryCondParam;
import com.nft.collection.param.PayOrderQueryCondParam;
import com.nft.collection.param.ResaleCollectionCreateOrderParam;
import com.nft.collection.param.UpdateCollectionStoryParam;
import com.nft.collection.repo.CollectionGiveRecordRepo;
import com.nft.collection.repo.CollectionRepo;
import com.nft.collection.repo.CollectionStoryRepo;
import com.nft.collection.repo.CreatorRepo;
import com.nft.collection.repo.MemberHoldCollectionRepo;
import com.nft.collection.repo.PayOrderRepo;
import com.nft.collection.vo.CollectionGiveRecordVO;
import com.nft.collection.vo.CollectionVO;
import com.nft.collection.vo.CreatorVO;
import com.nft.collection.vo.ForSaleCollectionVO;
import com.nft.collection.vo.GroupByDateCollectionVO;
import com.nft.collection.vo.GroupByTimeCollectionVO;
import com.nft.collection.vo.LatestCollectionDetailVO;
import com.nft.collection.vo.LatestCollectionVO;
import com.nft.collection.vo.MemberHoldCollectionVO;
import com.nft.collection.vo.MyGiveRecordVO;
import com.nft.collection.vo.MyHoldCollectionDetailVO;
import com.nft.collection.vo.MyHoldCollectionVO;
import com.nft.collection.vo.MyPayOrderDetailVO;
import com.nft.collection.vo.MyPayOrderVO;
import com.nft.collection.vo.MyResaleCollectionDetailVO;
import com.nft.collection.vo.MyResaleCollectionVO;
import com.nft.collection.vo.MySoldCollectionVO;
import com.nft.collection.vo.ResaleCollectionDetailVO;
import com.nft.collection.vo.ResaleCollectionVO;
import com.nft.collection.vo.SecondaryMarketCollectionVO;
import com.nft.common.exception.BizException;
import com.nft.common.utils.IdUtils;
import com.nft.common.vo.PageResult;
import com.nft.constants.Constant;
import com.nft.member.domain.Member;
import com.nft.member.repo.MemberRepo;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;

@Validated
@Service
public class CollectionService {

	@Autowired
	private CollectionRepo collectionRepo;

	@Autowired
	private CollectionStoryRepo collectionStoryRepo;

	@Autowired
	private CreatorRepo creatorRepo;

	@Autowired
	private MemberRepo memberRepo;

	@Autowired
	private MemberHoldCollectionRepo memberHoldCollectionRepo;

	@Autowired
	private CollectionGiveRecordRepo collectionGiveRecordRepo;

	@Autowired
	private PayOrderRepo payOrderRepo;

	@Transactional(readOnly = true)
	public MyPayOrderDetailVO findMyPayOrderDetail(@NotBlank String id) {
		PayOrder order = payOrderRepo.getOne(id);
		return MyPayOrderDetailVO.convertFor(order);
	}

	@Transactional(readOnly = true)
	public PageResult<MyPayOrderVO> findMyPayOrderByPage(@Valid PayOrderQueryCondParam param) {
		Page<PayOrder> result = payOrderRepo.findAll(param.buildSpecification(),
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("createTime"))));
		PageResult<MyPayOrderVO> pageResult = new PageResult<>(MyPayOrderVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

	@Transactional
	public void cancelPay(@Valid CancelPayParam param) {
		PayOrder payOrder = payOrderRepo.getOne(param.getOrderId());
		if (!Constant.支付订单状态_待支付.equals(payOrder.getState())) {
			throw new BizException("订单状态异常");
		}
		if (!payOrder.getMemberId().equals(param.getMemberId())) {
			throw new BizException("订单归属异常");
		}
		if (Constant.支付订单业务类型_首发藏品.equals(payOrder.getBizType())) {
			cancelPayWithLatestCollection(payOrder);
		} else if (Constant.支付订单业务类型_二级市场藏品.equals(payOrder.getBizType())) {
			cancelPayWithResaleCollection(payOrder);
		}
	}

	public void cancelPayWithLatestCollection(PayOrder payOrder) {
		payOrder.cancelOrder();
		payOrderRepo.save(payOrder);
	}

	public void cancelPayWithResaleCollection(PayOrder payOrder) {
		payOrder.cancelOrder();
		payOrderRepo.save(payOrder);
	}

	@Transactional
	public void confirmPay(@Valid ConfirmPayParam param) {
		PayOrder payOrder = payOrderRepo.getOne(param.getOrderId());
		if (!Constant.支付订单状态_待支付.equals(payOrder.getState())) {
			throw new BizException("订单状态异常");
		}
		if (!payOrder.getMemberId().equals(param.getMemberId())) {
			throw new BizException("订单归属异常");
		}
		if (Constant.支付订单业务类型_首发藏品.equals(payOrder.getBizType())) {
			confirmPayWithLatestCollection(payOrder);
		} else if (Constant.支付订单业务类型_二级市场藏品.equals(payOrder.getBizType())) {
			confirmPayWithResaleCollection(payOrder);
		}
	}

	public void confirmPayWithResaleCollection(PayOrder payOrder) {
		Member buyer = payOrder.getMember();
		buyer.validBasicRisk();
		double balance = NumberUtil.round(buyer.getBalance() - payOrder.getAmount(), 2).doubleValue();
		if (balance < 0) {
			throw new BizException("余额不足");
		}

		buyer.setBalance(balance);
		memberRepo.save(buyer);

		MemberHoldCollection resaleCollection = memberHoldCollectionRepo.getOne(payOrder.getBizOrderNo());
		MemberHoldCollection buyerCollection = resaleCollection.buildWithResale(buyer.getId());
		memberHoldCollectionRepo.save(buyerCollection);

		resaleCollection.setLoseTime(new Date());
		resaleCollection.setState(Constant.持有藏品状态_已卖出);
		memberHoldCollectionRepo.save(resaleCollection);

		payOrder.paid();
		payOrderRepo.save(payOrder);
	}

	public void confirmPayWithLatestCollection(PayOrder payOrder) {
		Member member = payOrder.getMember();
		member.validBasicRisk();
		Collection collection = payOrder.getCollection();
		if (collection.getStock() <= 0) {
			throw new BizException("已售罄");
		}
		double balance = NumberUtil.round(member.getBalance() - collection.getPrice(), 2).doubleValue();
		if (balance < 0) {
			throw new BizException("余额不足");
		}

		collection.setStock(collection.getStock() - 1);
		collectionRepo.save(collection);

		MemberHoldCollection memberHoldCollection = collection.buy(member.getId());
		memberHoldCollectionRepo.save(memberHoldCollection);

		member.setBalance(balance);
		memberRepo.save(member);

		payOrder.paid();
		payOrderRepo.save(payOrder);
	}

	@Transactional
	public String resaleCollectionCreateOrder(ResaleCollectionCreateOrderParam param) {
		MemberHoldCollection resaleCollection = memberHoldCollectionRepo.getOne(param.getResaleCollectionId());
		if (!Constant.持有藏品状态_转售中.equals(resaleCollection.getState())) {
			throw new BizException("藏品归属异常");
		}
		Member buyer = memberRepo.getOne(param.getMemberId());
		if (buyer.getId().equals(resaleCollection.getMemberId())) {
			throw new BizException("不能购买自己的藏品");
		}

		PayOrder payOrder = PayOrder.buildWithResaleCollection(resaleCollection, buyer);
		payOrderRepo.save(payOrder);
		return payOrder.getId();
	}

	@Transactional
	public String latestCollectionCreateOrder(LatestCollectionCreateOrderParam param) {
		Member member = memberRepo.getOne(param.getMemberId());
		member.validBasicRisk();
		Collection collection = collectionRepo.getOne(param.getCollectionId());
		if (System.currentTimeMillis() - collection.getSaleTime().getTime() < 0) {
			throw new BizException("未到发售时间");
		}
		if (collection.getStock() <= 0) {
			throw new BizException("已售罄");
		}
		PayOrder payOrder = PayOrder.buildWithLatestCollection(collection, member);
		payOrderRepo.save(payOrder);
		return payOrder.getId();
	}

	@Transactional(readOnly = true)
	public PageResult<MyGiveRecordVO> findMyGiveRecordByPage(@Valid CollectionGiveRecordQueryCondParam param) {
		Page<CollectionGiveRecord> result = collectionGiveRecordRepo.findAll(param.buildSpecification(),
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("giveTime"))));
		PageResult<MyGiveRecordVO> pageResult = new PageResult<>(
				MyGiveRecordVO.convertFor(result.getContent(), param.getMemberId()), param.getPageNum(),
				param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

	@Transactional(readOnly = true)
	public PageResult<CollectionGiveRecordVO> findCollectionGiveRecordByPage(
			@Valid CollectionGiveRecordQueryCondParam param) {
		Page<CollectionGiveRecord> result = collectionGiveRecordRepo.findAll(param.buildSpecification(),
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("giveTime"))));
		PageResult<CollectionGiveRecordVO> pageResult = new PageResult<>(
				CollectionGiveRecordVO.convertFor(result.getContent()), param.getPageNum(), param.getPageSize(),
				result.getTotalElements());
		return pageResult;
	}

	@Transactional(readOnly = true)
	public String getGiveToAccountNickName(@NotBlank String giveToAccount, @NotBlank String giveFromId) {
		Member giveTo = memberRepo.findByMobileAndDeletedFlagIsFalse(giveToAccount);
		if (giveTo == null) {
			giveTo = memberRepo.findByBlockChainAddrAndDeletedFlagIsFalse(giveToAccount);
		}
		if (giveTo == null) {
			throw new BizException("请检查账号是否正确");
		}
		if (giveTo.getId().equals(giveFromId)) {
			throw new BizException("不能转给自己");
		}
		return giveTo.getNickName();
	}

	@Transactional
	public void cancelResale(CollectionCancelResaleParam param) {
		MemberHoldCollection holdCollection = memberHoldCollectionRepo.getOne(param.getHoldCollectionId());
		if (!param.getMemberId().equals(holdCollection.getMemberId())) {
			throw new BizException("无权操作该藏品");
		}
		if (!Constant.持有藏品状态_转售中.equals(holdCollection.getState())) {
			throw new BizException("藏品归属异常");
		}
		holdCollection.cancelResale();
		memberHoldCollectionRepo.save(holdCollection);
	}

	@Transactional
	public void collectionResale(CollectionResaleParam param) {
		MemberHoldCollection holdCollection = memberHoldCollectionRepo.getOne(param.getHoldCollectionId());
		if (!param.getMemberId().equals(holdCollection.getMemberId())) {
			throw new BizException("无权操作该藏品");
		}
		if (!Constant.持有藏品状态_持有中.equals(holdCollection.getState())) {
			throw new BizException("藏品归属异常");
		}
		holdCollection.resale(param.getResalePrice());
		memberHoldCollectionRepo.save(holdCollection);
	}

	@Transactional
	public void collectionGive(CollectionGiveParam param) {
		MemberHoldCollection holdCollection = memberHoldCollectionRepo.getOne(param.getHoldCollectionId());
		if (!param.getMemberId().equals(holdCollection.getMemberId())) {
			throw new BizException("无权操作该藏品");
		}
		if (!Constant.持有藏品状态_持有中.equals(holdCollection.getState())) {
			throw new BizException("藏品归属异常");
		}
		Member giveTo = memberRepo.findByMobileAndDeletedFlagIsFalse(param.getGiveToAccount());
		if (giveTo == null) {
			giveTo = memberRepo.findByBlockChainAddrAndDeletedFlagIsFalse(param.getGiveToAccount());
		}
		if (giveTo == null) {
			throw new BizException("账号不存在");
		}
		if (giveTo.getId().equals(param.getMemberId())) {
			throw new BizException("不能转给自己");
		}

		CollectionGiveRecord collectionGiveRecord = CollectionGiveRecord.build(holdCollection.getId(),
				holdCollection.getMemberId(), giveTo.getId());
		collectionGiveRecordRepo.save(collectionGiveRecord);

		MemberHoldCollection receiveCollection = holdCollection.buildWithGive(giveTo.getId());
		memberHoldCollectionRepo.save(receiveCollection);

		holdCollection.setLoseTime(new Date());
		holdCollection.setState(Constant.持有藏品状态_已转赠);
		memberHoldCollectionRepo.save(holdCollection);
	}

	@Transactional(readOnly = true)
	public PageResult<MemberHoldCollectionVO> findMemberHoldCollectionByPage(
			@Valid MemberHoldCollectionQueryCondParam param) {
		param.setState(Constant.持有藏品状态_持有中);
		Page<MemberHoldCollection> result = memberHoldCollectionRepo.findAll(param.buildSpecification(),
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("holdTime"))));
		PageResult<MemberHoldCollectionVO> pageResult = new PageResult<>(
				MemberHoldCollectionVO.convertFor(result.getContent()), param.getPageNum(), param.getPageSize(),
				result.getTotalElements());
		return pageResult;
	}

	@Transactional(readOnly = true)
	public ResaleCollectionDetailVO findResaleCollectionDetail(@NotBlank String id) {
		MemberHoldCollection memberHoldCollection = memberHoldCollectionRepo.getOne(id);
		return ResaleCollectionDetailVO.convertFor(memberHoldCollection);
	}

	@Transactional(readOnly = true)
	public MyResaleCollectionDetailVO findMyResaleCollectionDetail(@NotBlank String id, @NotBlank String memberId) {
		MemberHoldCollection memberHoldCollection = memberHoldCollectionRepo.getOne(id);
		if (!memberId.equals(memberHoldCollection.getMemberId())) {
			throw new BizException("无权操作");
		}
		return MyResaleCollectionDetailVO.convertFor(memberHoldCollection);
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
	public PageResult<MySoldCollectionVO> findMySoldCollectionByPage(@Valid MemberHoldCollectionQueryCondParam param) {
		param.setState(Constant.持有藏品状态_已卖出);
		Page<MemberHoldCollection> result = memberHoldCollectionRepo.findAll(param.buildSpecification(),
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("loseTime"))));
		PageResult<MySoldCollectionVO> pageResult = new PageResult<>(MySoldCollectionVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

	@Transactional(readOnly = true)
	public PageResult<SecondaryMarketCollectionVO> findSecondaryMarketCollectionByPage(
			@Valid MemberHoldCollectionQueryCondParam param) {
		param.setState(Constant.持有藏品状态_转售中);
		Page<MemberHoldCollection> result = memberHoldCollectionRepo.findAll(param.buildSpecification(),
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("resaleTime"))));
		PageResult<SecondaryMarketCollectionVO> pageResult = new PageResult<>(
				SecondaryMarketCollectionVO.convertFor(result.getContent()), param.getPageNum(), param.getPageSize(),
				result.getTotalElements());
		return pageResult;
	}

	@Transactional(readOnly = true)
	public PageResult<ResaleCollectionVO> findResaleCollectionByPage(@Valid MemberHoldCollectionQueryCondParam param) {
		param.setState(Constant.持有藏品状态_转售中);
		Page<MemberHoldCollection> result = memberHoldCollectionRepo.findAll(param.buildSpecification(),
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("resaleTime"))));
		PageResult<ResaleCollectionVO> pageResult = new PageResult<>(ResaleCollectionVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

	@Transactional(readOnly = true)
	public PageResult<MyResaleCollectionVO> findMyResaleCollectionByPage(
			@Valid MemberHoldCollectionQueryCondParam param) {
		param.setState(Constant.持有藏品状态_转售中);
		Page<MemberHoldCollection> result = memberHoldCollectionRepo.findAll(param.buildSpecification(),
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("resaleTime"))));
		PageResult<MyResaleCollectionVO> pageResult = new PageResult<>(
				MyResaleCollectionVO.convertFor(result.getContent()), param.getPageNum(), param.getPageSize(),
				result.getTotalElements());
		return pageResult;
	}

	@Transactional(readOnly = true)
	public PageResult<MyHoldCollectionVO> findMyHoldCollectionByPage(@Valid MemberHoldCollectionQueryCondParam param) {
		param.setState(Constant.持有藏品状态_持有中);
		Page<MemberHoldCollection> result = memberHoldCollectionRepo.findAll(param.buildSpecification(),
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("holdTime"))));
		PageResult<MyHoldCollectionVO> pageResult = new PageResult<>(MyHoldCollectionVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

	@Transactional
	public void delCreator(@NotBlank String id) {
		Creator creator = creatorRepo.getOne(id);
		creator.deleted();
		creatorRepo.save(creator);
	}

	@Transactional(readOnly = true)
	public CreatorVO findCreatorById(@NotBlank String id) {
		Creator creator = creatorRepo.getOne(id);
		return CreatorVO.convertFor(creator);
	}

	@Transactional(readOnly = true)
	public List<CreatorVO> findAllCreator() {
		List<Creator> result = creatorRepo.findAll(new CreatorQueryCondParam().buildSpecification(),
				Sort.by(Sort.Order.desc("createTime")));
		return CreatorVO.convertFor(result);
	}

	@Transactional(readOnly = true)
	public PageResult<CreatorVO> findCreatorByPage(@Valid CreatorQueryCondParam param) {
		Page<Creator> result = creatorRepo.findAll(param.buildSpecification(),
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("createTime"))));
		PageResult<CreatorVO> pageResult = new PageResult<>(CreatorVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

	@Transactional
	public void addOrUpdateCreator(@Valid AddOrUpdateCreatorParam param) {
		if (StrUtil.isBlank(param.getId())) {
			Creator creator = param.convertToPo();
			creatorRepo.save(creator);
		} else {
			Creator creator = creatorRepo.getOne(param.getId());
			BeanUtils.copyProperties(param, creator);
			creator.setLastModifyTime(new Date());
			creatorRepo.save(creator);
		}
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
	public PageResult<LatestCollectionVO> findLatestCollectionByPage(@Valid CollectionQueryCondParam param) {
		param.setSaleTimeEnd(DateUtil.offset(new Date(), DateField.DAY_OF_YEAR, 2).toJdkDate());
		Page<Collection> result = collectionRepo.findAll(param.buildSpecification(),
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("saleTime"))));
		PageResult<LatestCollectionVO> pageResult = new PageResult<>(LatestCollectionVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

	@Transactional(readOnly = true)
	public PageResult<CollectionVO> findCollectionByPage(@Valid CollectionQueryCondParam param) {
		Page<Collection> result = collectionRepo.findAll(param.buildSpecification(),
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("createTime"))));
		PageResult<CollectionVO> pageResult = new PageResult<>(CollectionVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
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
	}
}
