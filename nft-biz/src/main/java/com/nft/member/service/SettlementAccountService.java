package com.nft.member.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.nft.common.exception.BizError;
import com.nft.common.exception.BizException;
import com.nft.constants.Constant;
import com.nft.member.domain.Member;
import com.nft.member.domain.SettlementAccount;
import com.nft.member.param.AddSettlementAccountParam;
import com.nft.member.param.SettlementAccountQueryCondParam;
import com.nft.member.param.UpdateSettlementAccountStateParam;
import com.nft.member.repo.MemberRepo;
import com.nft.member.repo.SettlementAccountRepo;
import com.nft.member.vo.SettlementAccountVO;

import cn.hutool.core.util.StrUtil;

@Validated
@Service
public class SettlementAccountService {

	@Autowired
	private SettlementAccountRepo settlementAccountRepo;

	@Autowired
	private MemberRepo memberRepo;

	@Transactional
	public void updateActivatedFlag(@Valid UpdateSettlementAccountStateParam param) {
		SettlementAccount settlementAccount = settlementAccountRepo.getOne(param.getId());
		if (!settlementAccount.getMemberId().equals(param.getMemberId())) {
			throw new BizException("操作异常");
		}
		List<SettlementAccount> activatedPaymentReceivedInfos = settlementAccountRepo
				.findByMemberIdAndActivatedTrueAndDeletedFlagFalseOrderByActivatedTimeDesc(param.getMemberId());
		if (activatedPaymentReceivedInfos.size() >= 3 && param.getActivated()) {
			throw new BizException("最多只能激活3个结算账户");
		}

		settlementAccount.setActivated(param.getActivated());
		settlementAccount.setActivatedTime(settlementAccount.getActivated() ? new Date() : null);
		settlementAccountRepo.save(settlementAccount);
	}

	public Specification<SettlementAccount> buildQueryCond(SettlementAccountQueryCondParam param) {
		Specification<SettlementAccount> spec = new Specification<SettlementAccount>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<SettlementAccount> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				predicates.add(builder.equal(root.get("deletedFlag"), false));
				if (param.getActivated() != null) {
					predicates.add(builder.equal(root.get("activated"), param.getActivated()));
				}
				if (StrUtil.isNotBlank(param.getMemberId())) {
					predicates.add(builder.equal(root.get("memberId"), param.getMemberId()));
				}
				if (StrUtil.isNotBlank(param.getType())) {
					predicates.add(root.get("type").in(Arrays.asList(param.getType().split(","))));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		return spec;
	}

	@Transactional(readOnly = true)
	public List<SettlementAccountVO> findAll(@Valid SettlementAccountQueryCondParam param) {
		Specification<SettlementAccount> spec = buildQueryCond(param);
		List<SettlementAccount> receiptPaymentInfos = settlementAccountRepo.findAll(spec,
				Sort.by(Sort.Order.desc("activatedTime")));
		return SettlementAccountVO.convertFor(receiptPaymentInfos);
	}

	@Transactional
	public void del(@NotBlank String id, @NotBlank String memberId) {
		SettlementAccount settlementAccount = settlementAccountRepo.getOne(id);
		if (!settlementAccount.getMemberId().equals(memberId)) {
			throw new BizException("操作异常");
		}
		del(id);
	}

	@Transactional
	public void del(@NotBlank String id) {
		SettlementAccount settlementAccount = settlementAccountRepo.getOne(id);
		settlementAccount.deleted();
		settlementAccountRepo.save(settlementAccount);
	}

	@Transactional
	public void add(@Valid AddSettlementAccountParam param) {
		Member member = memberRepo.getOne(param.getMemberId());
		member.validBasicRisk();
		if (Constant.结算账户_银行卡.equals(param.getType())) {
			if (StrUtil.isBlank(param.getCardNumber()) || StrUtil.isBlank(param.getBankName())) {
				throw new BizException(BizError.参数异常);
			}
		}
		if (Constant.结算账户_支付宝.equals(param.getType()) || Constant.结算账户_微信.equals(param.getType())) {
			if (StrUtil.isBlank(param.getAccount())) {
				throw new BizException(BizError.参数异常);
			}
		}
		List<SettlementAccount> all = settlementAccountRepo
				.findByMemberIdAndDeletedFlagFalseOrderByActivatedTimeDesc(param.getMemberId());
		if (all.size() >= 5) {
			throw new BizException("最多只能添加5个结算账户");
		}
		SettlementAccount settlementAccount = param.convertToPo();
		settlementAccount.setRealName(member.getRealName());
		settlementAccountRepo.save(settlementAccount);
	}

}
