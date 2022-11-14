package com.nft.collection.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import com.nft.collection.domain.IssuedCollection;
import com.nft.collection.domain.IssuedCollectionActionLog;
import com.nft.collection.domain.MemberHoldCollection;
import com.nft.collection.domain.MemberResaleCollection;
import com.nft.collection.domain.MysteryBoxCommodity;
import com.nft.collection.domain.PayOrder;
import com.nft.collection.param.CancelPayParam;
import com.nft.collection.param.CollectionCancelResaleParam;
import com.nft.collection.param.CollectionResaleParam;
import com.nft.collection.param.ConfirmPayParam;
import com.nft.collection.param.LatestCollectionCreateOrderParam;
import com.nft.collection.param.OpenMysteryBoxParam;
import com.nft.collection.param.PayOrderQueryCondParam;
import com.nft.collection.param.ResaleCollectionCreateOrderParam;
import com.nft.collection.repo.CollectionRepo;
import com.nft.collection.repo.IssuedCollectionActionLogRepo;
import com.nft.collection.repo.IssuedCollectionRepo;
import com.nft.collection.repo.MemberHoldCollectionRepo;
import com.nft.collection.repo.MemberResaleCollectionRepo;
import com.nft.collection.repo.PayOrderRepo;
import com.nft.collection.vo.MyPayOrderDetailVO;
import com.nft.collection.vo.MyPayOrderVO;
import com.nft.collection.vo.OpenMysteryBoxResultVO;
import com.nft.collection.vo.PayOrderVO;
import com.nft.common.exception.BizException;
import com.nft.common.utils.ThreadPoolUtils;
import com.nft.common.vo.PageResult;
import com.nft.constants.Constant;
import com.nft.log.domain.MemberBalanceChangeLog;
import com.nft.log.repo.MemberBalanceChangeLogRepo;
import com.nft.member.domain.Member;
import com.nft.member.repo.MemberRepo;
import com.zengtengpeng.annotation.Lock;

import cn.hutool.core.lang.WeightRandom.WeightObj;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;

@Validated
@Service
public class TransactionService {

	@Autowired
	private RedissonClient redissonClient;

	@Autowired
	private CollectionRepo collectionRepo;

	@Autowired
	private MemberHoldCollectionRepo memberHoldCollectionRepo;

	@Autowired
	private MemberRepo memberRepo;

	@Autowired
	private IssuedCollectionRepo issuedCollectionRepo;

	@Autowired
	private PayOrderRepo payOrderRepo;

	@Autowired
	private MemberBalanceChangeLogRepo memberBalanceChangeLogRepo;

	@Autowired
	private MemberResaleCollectionRepo memberResaleCollectionRepo;

	@Autowired
	private IssuedCollectionActionLogRepo issuedCollectionActionLogRepo;

	@Transactional(readOnly = true)
	public MyPayOrderDetailVO findMyPayOrderDetail(@NotBlank String id) {
		PayOrder order = payOrderRepo.getOne(id);
		return MyPayOrderDetailVO.convertFor(order);
	}

	@Transactional(readOnly = true)
	public PageResult<PayOrderVO> findPayOrderByPage(@Valid PayOrderQueryCondParam param) {
		Page<PayOrder> result = payOrderRepo.findAll(param.buildSpecification(),
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("createTime"))));
		PageResult<PayOrderVO> pageResult = new PageResult<>(PayOrderVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
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
	public void payOrderTimeoutCancel() {
		Date now = new Date();
		List<PayOrder> payOrders = payOrderRepo.findByStateAndOrderDeadlineLessThan(Constant.支付订单状态_待付款, now);
		for (PayOrder payOrder : payOrders) {
			redissonClient.getTopic(Constant.支付订单超时取消).publish(payOrder.getId());
		}
	}

	@Transactional
	public void cancelPay(@Valid CancelPayParam param) {
		PayOrder payOrder = payOrderRepo.getOne(param.getOrderId());
		if (!payOrder.getMemberId().equals(param.getMemberId())) {
			throw new BizException("订单归属异常");
		}
		cancelPay(param.getOrderId());
	}

	@Transactional
	public void cancelPay(String orderId) {
		PayOrder payOrder = payOrderRepo.getOne(orderId);
		if (!Constant.支付订单状态_待付款.equals(payOrder.getState())) {
			throw new BizException("订单状态异常");
		}
		if (Constant.支付订单业务模式_平台自营.equals(payOrder.getBizMode())) {
			cancelPayWithLatestCollection(payOrder);
		} else if (Constant.支付订单业务类型_二级市场.equals(payOrder.getBizMode())) {
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

		MemberResaleCollection memberResaleCollection = memberResaleCollectionRepo.getOne(payOrder.getBizOrderNo());
		if (StrUtil.isNotBlank(memberResaleCollection.getLockPayMemberId())) {
			memberResaleCollection.setLockPayMemberId(null);
			memberResaleCollectionRepo.save(memberResaleCollection);
		}
	}

	@Transactional
	public void confirmPay(@Valid ConfirmPayParam param) {
		PayOrder payOrder = payOrderRepo.getOne(param.getOrderId());
		if (!Constant.支付订单状态_待付款.equals(payOrder.getState())) {
			throw new BizException("订单状态异常");
		}
		if (!payOrder.getMemberId().equals(param.getMemberId())) {
			throw new BizException("订单归属异常");
		}
		if (Constant.支付订单业务模式_平台自营.equals(payOrder.getBizMode())) {
			confirmPayWithLatestCollection(payOrder);
		} else if (Constant.支付订单业务类型_二级市场.equals(payOrder.getBizMode())) {
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

		MemberResaleCollection memberResaleCollection = memberResaleCollectionRepo.getOne(payOrder.getBizOrderNo());
		memberResaleCollection.sold();
		memberResaleCollectionRepo.save(memberResaleCollection);

		MemberHoldCollection sellerHoldCollection = memberResaleCollection.getMemberHoldCollection();
		sellerHoldCollection.sold();
		memberHoldCollectionRepo.save(sellerHoldCollection);

		MemberHoldCollection buyerHoldCollection = sellerHoldCollection.buildWithBuyerHold(buyer.getId(),
				payOrder.getAmount());
		buyerHoldCollection.setPreId(sellerHoldCollection.getId());
		memberHoldCollectionRepo.save(buyerHoldCollection);

		issuedCollectionActionLogRepo.save(IssuedCollectionActionLog.buildWithBuy(payOrder.getAmount(), buyer.getId(),
				memberResaleCollection.getIssuedCollectionId()));

		Member seller = sellerHoldCollection.getMember();
		seller.setBalance(NumberUtil.round(seller.getBalance() + payOrder.getAmount(), 2).doubleValue());
		memberRepo.save(seller);

		if (!buyer.getBoughtFlag()) {
			buyer.setBoughtFlag(true);
		}
		buyer.setBalance(balance);
		memberRepo.save(buyer);

		payOrder.paid();
		payOrderRepo.save(payOrder);

		memberBalanceChangeLogRepo.save(MemberBalanceChangeLog.buildWithSellCollection(seller, payOrder));
		memberBalanceChangeLogRepo.save(MemberBalanceChangeLog.buildWithBuyResaleCollection(buyer, payOrder));

		ThreadPoolUtils.getSyncChainPool().schedule(() -> {
			redissonClient.getTopic(Constant.上链_二级市场购买艺术品)
					.publish(buyerHoldCollection.getId());
		}, 1, TimeUnit.SECONDS);
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

		IssuedCollection issuedCollection = collection.issue();
		issuedCollectionRepo.save(issuedCollection);

		issuedCollectionActionLogRepo.save(
				IssuedCollectionActionLog.buildWithBuy(payOrder.getAmount(), member.getId(), issuedCollection.getId()));

		MemberHoldCollection memberHoldCollection = issuedCollection.firstIssueToMember(member.getId(),
				collection.getPrice(), Constant.藏品获取方式_购买);
		memberHoldCollectionRepo.save(memberHoldCollection);

		if (!member.getBoughtFlag()) {
			member.setBoughtFlag(true);
		}
		member.setBalance(balance);
		memberRepo.save(member);

		payOrder.paid();
		payOrderRepo.save(payOrder);

		memberBalanceChangeLogRepo.save(MemberBalanceChangeLog.buildWithBuyLatestCollection(member, payOrder));

		ThreadPoolUtils.getSyncChainPool().schedule(() -> {
			redissonClient.getTopic(Constant.上链_铸造艺术品).publish(memberHoldCollection.getId());
		}, 1, TimeUnit.SECONDS);
	}

	@Lock(keys = "'resaleCollectionCreateOrder' + #param.resaleCollectionId")
	@Transactional
	public String resaleCollectionCreateOrder(@Valid ResaleCollectionCreateOrderParam param) {
		MemberResaleCollection resaleCollection = memberResaleCollectionRepo.getOne(param.getResaleCollectionId());
		if (!Constant.转售的藏品状态_已发布.equals(resaleCollection.getState())) {
			throw new BizException("藏品状态异常");
		}
		if (StrUtil.isNotBlank(resaleCollection.getLockPayMemberId())
				&& !resaleCollection.getLockPayMemberId().equals(param.getMemberId())) {
			throw new BizException("商品已被锁定");
		}
		PayOrder pendingOrder = payOrderRepo.findTopByMemberIdAndCollectionIdAndBizOrderNoAndState(param.getMemberId(),
				resaleCollection.getCollectionId(), resaleCollection.getId(), Constant.支付订单状态_待付款);
		if (pendingOrder != null) {
			return pendingOrder.getId();
		}
		Member buyer = memberRepo.getOne(param.getMemberId());
		buyer.validBasicRisk();
		if (buyer.getId().equals(resaleCollection.getMemberId())) {
			throw new BizException("不能购买自己的藏品");
		}

		if (StrUtil.isBlank(resaleCollection.getLockPayMemberId())) {
			resaleCollection.setLockPayMemberId(param.getMemberId());
			memberResaleCollectionRepo.save(resaleCollection);
		}

		PayOrder payOrder = PayOrder.buildWithResaleCollection(resaleCollection, buyer);
		payOrderRepo.save(payOrder);
		return payOrder.getId();
	}

	@Lock(keys = "'latestCollectionCreateOrder' + #param.memberId + #param.collectionId")
	@Transactional
	public String latestCollectionCreateOrder(@Valid LatestCollectionCreateOrderParam param) {
		PayOrder pendingOrder = payOrderRepo.findTopByMemberIdAndCollectionIdAndState(param.getMemberId(),
				param.getCollectionId(), Constant.支付订单状态_待付款);
		if (pendingOrder != null) {
			return pendingOrder.getId();
		}
		Member member = memberRepo.getOne(param.getMemberId());
		member.validBasicRisk();
		Collection collection = collectionRepo.getOne(param.getCollectionId());
		if (!collection.getExternalSaleFlag()) {
			throw new BizException("不对外发售");
		}
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

	@Transactional
	public void cancelResale(@Valid CollectionCancelResaleParam param) {
		MemberResaleCollection resaleCollection = memberResaleCollectionRepo.getOne(param.getResaleCollectionId());
		if (!param.getMemberId().equals(resaleCollection.getMemberId())) {
			throw new BizException("无权操作该藏品");
		}
		if (!Constant.转售的藏品状态_已发布.equals(resaleCollection.getState())) {
			throw new BizException("藏品归属异常");
		}
		if (StrUtil.isNotBlank(resaleCollection.getLockPayMemberId())) {
			throw new BizException("锁定支付中,不能取消");
		}

		resaleCollection.cancelResale();
		memberResaleCollectionRepo.save(resaleCollection);

		MemberHoldCollection memberHoldCollection = resaleCollection.getMemberHoldCollection();
		memberHoldCollection.setState(Constant.持有藏品状态_持有中);
		memberHoldCollectionRepo.save(memberHoldCollection);

		issuedCollectionActionLogRepo.save(IssuedCollectionActionLog.buildWithCancelResale(
				memberHoldCollection.getMemberId(), memberHoldCollection.getIssuedCollectionId()));
	}

	@Transactional
	public OpenMysteryBoxResultVO openMysteryBox(OpenMysteryBoxParam param) {
		MemberHoldCollection mysteryBox = memberHoldCollectionRepo.getOne(param.getHoldCollectionId());
		if (!param.getMemberId().equals(mysteryBox.getMemberId())) {
			throw new BizException("无权操作该商品");
		}
		if (!Constant.持有藏品状态_持有中.equals(mysteryBox.getState())) {
			throw new BizException("商品归属异常");
		}
		if (!Constant.商品类型_盲盒.equals(mysteryBox.getCollection().getCommodityType())) {
			throw new BizException("该商品不属于盲盒");
		}
		List<WeightObj<MysteryBoxCommodity>> weightCommoditys = new ArrayList<>();
		for (MysteryBoxCommodity mysteryBoxCommodity : mysteryBox.getCollection().getSubCommoditys()) {
			weightCommoditys.add(new WeightObj<>(mysteryBoxCommodity, mysteryBoxCommodity.getProbability()));
		}

		MysteryBoxCommodity randomCommodity = RandomUtil.weightRandom(weightCommoditys).next();
		Collection collection = randomCommodity.getCommodity();
		if (collection.getStock() <= 0) {
			throw new BizException("盲盒太火了，请稍后再打开");
		}

		collection.setStock(collection.getStock() - 1);
		collectionRepo.save(collection);

		issuedCollectionActionLogRepo.save(IssuedCollectionActionLog.buildWithOpenMysteryBox(param.getMemberId(),
				mysteryBox.getIssuedCollectionId()));
		mysteryBox.openMysteryBoxDestroy();
		memberHoldCollectionRepo.save(mysteryBox);
		
		IssuedCollection mysteryBoxIssuedCollection = mysteryBox.getIssuedCollection();
		mysteryBoxIssuedCollection.deleted();
		issuedCollectionRepo.save(mysteryBoxIssuedCollection);

		IssuedCollection issuedCollection = collection.issue();
		issuedCollectionRepo.save(issuedCollection);

		issuedCollectionActionLogRepo
				.save(IssuedCollectionActionLog.buildWithMysteryBoxGet(param.getMemberId(), issuedCollection.getId()));

		MemberHoldCollection memberHoldCollection = issuedCollection.firstIssueToMember(param.getMemberId(),
				collection.getPrice(), Constant.藏品获取方式_盲盒);
		memberHoldCollectionRepo.save(memberHoldCollection);

		ThreadPoolUtils.getSyncChainPool().schedule(() -> {
			redissonClient.getTopic(Constant.上链_销毁艺术品).publish(mysteryBox.getId());
			redissonClient.getTopic(Constant.上链_铸造艺术品).publish(memberHoldCollection.getId());
		}, 1, TimeUnit.SECONDS);

		return OpenMysteryBoxResultVO.build(collection.getName(), collection.getCover());
	}

	@Transactional
	public void collectionResale(@Valid CollectionResaleParam param) {
		MemberHoldCollection memberHoldCollection = memberHoldCollectionRepo.getOne(param.getHoldCollectionId());
		if (!param.getMemberId().equals(memberHoldCollection.getMemberId())) {
			throw new BizException("无权操作该藏品");
		}
		if (!Constant.持有藏品状态_持有中.equals(memberHoldCollection.getState())) {
			throw new BizException("藏品归属异常");
		}
		memberHoldCollection.setState(Constant.持有藏品状态_转售中);
		memberHoldCollectionRepo.save(memberHoldCollection);

		MemberResaleCollection resaleCollection = memberHoldCollection
				.buildResaleCollectionReocrd(param.getResalePrice());
		memberResaleCollectionRepo.save(resaleCollection);

		issuedCollectionActionLogRepo.save(IssuedCollectionActionLog.buildWithResale(resaleCollection.getResalePrice(),
				memberHoldCollection.getMemberId(), memberHoldCollection.getIssuedCollectionId()));
	}

}
