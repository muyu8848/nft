package com.nft.member.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.nft.common.exception.BizException;
import com.nft.common.vo.PageResult;
import com.nft.constants.Constant;
import com.nft.log.domain.MemberBalanceChangeLog;
import com.nft.log.repo.MemberBalanceChangeLogRepo;
import com.nft.member.domain.Member;
import com.nft.member.domain.SettlementAccount;
import com.nft.member.domain.WithdrawRecord;
import com.nft.member.param.WithdrawParam;
import com.nft.member.param.WithdrawRecordQueryCondParam;
import com.nft.member.repo.MemberRepo;
import com.nft.member.repo.SettlementAccountRepo;
import com.nft.member.repo.WithdrawRecordRepo;
import com.nft.member.vo.WithdrawRecordVO;
import com.zengtengpeng.annotation.Lock;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;

@Validated
@Service
public class WithdrawService {

	@Autowired
	private WithdrawRecordRepo withdrawRecordRepo;

	@Autowired
	private MemberRepo memberRepo;

	@Autowired
	private MemberBalanceChangeLogRepo memberBalanceChangeLogRepo;

	@Autowired
	private SettlementAccountRepo settlementAccountRepo;

	@Lock(keys = "'withdrawReject_' + #id")
	@Transactional
	public void reject(@NotBlank String id) {
		WithdrawRecord record = withdrawRecordRepo.getOne(id);
		if (!(Constant.提现记录状态_审核中.equals(record.getState()))) {
			throw new BizException("只有状态为审核中的记录才能进行驳回操作");
		}
		record.reject();
		withdrawRecordRepo.save(record);

		Member member = record.getMember();
		double balance = NumberUtil.round(member.getBalance() + record.getAmount(), 2).doubleValue();
		member.setBalance(balance);
		memberRepo.save(member);

		memberBalanceChangeLogRepo.save(MemberBalanceChangeLog.buildWithWithdrawReject(member, record));
	}

	@Lock(keys = "'memberWithdrawConfirmCredited_' + #id")
	@Transactional
	public void confirmCredited(@NotBlank String id) {
		WithdrawRecord record = withdrawRecordRepo.getOne(id);
		if (!(Constant.提现记录状态_审核中.equals(record.getState()))) {
			throw new BizException("只有状态为审核中的记录才能进行确认到帐操作");
		}
		record.confirmCredited();
		withdrawRecordRepo.save(record);
	}

	public Specification<WithdrawRecord> buildWithdrawRecordQueryCond(WithdrawRecordQueryCondParam param) {
		Specification<WithdrawRecord> spec = new Specification<WithdrawRecord>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<WithdrawRecord> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (StrUtil.isNotBlank(param.getOrderNo())) {
					predicates.add(builder.equal(root.get("orderNo"), param.getOrderNo()));
				}
				if (StrUtil.isNotBlank(param.getMemberId())) {
					predicates.add(builder.equal(root.get("memberId"), param.getMemberId()));
				}
				if (StrUtil.isNotBlank(param.getState())) {
					predicates.add(builder.equal(root.get("state"), param.getState()));
				}
				if (param.getSubmitTimeStart() != null) {
					predicates.add(builder.greaterThanOrEqualTo(root.get("submitTime").as(Date.class),
							DateUtil.beginOfDay(param.getSubmitTimeStart())));
				}
				if (param.getSubmitTimeEnd() != null) {
					predicates.add(builder.lessThanOrEqualTo(root.get("submitTime").as(Date.class),
							DateUtil.endOfDay(param.getSubmitTimeEnd())));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		return spec;
	}

	@Transactional(readOnly = true)
	public PageResult<WithdrawRecordVO> findByPage(@Valid WithdrawRecordQueryCondParam param) {
		Specification<WithdrawRecord> spec = buildWithdrawRecordQueryCond(param);
		Page<WithdrawRecord> result = withdrawRecordRepo.findAll(spec,
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("submitTime"))));
		PageResult<WithdrawRecordVO> pageResult = new PageResult<>(WithdrawRecordVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

	@Transactional(readOnly = true)
	public List<WithdrawRecordVO> findAll(WithdrawRecordQueryCondParam param) {
		Specification<WithdrawRecord> spec = buildWithdrawRecordQueryCond(param);
		List<WithdrawRecord> result = withdrawRecordRepo.findAll(spec, Sort.by(Sort.Order.desc("submitTime")));
		return WithdrawRecordVO.convertFor(result);
	}

	@Transactional
	public void withdraw(@Valid WithdrawParam param) {
		SettlementAccount settlementAccount = settlementAccountRepo
				.findByIdAndMemberIdAndDeletedFlagFalse(param.getSettlementAccountId(), param.getMemberId());
		if (settlementAccount == null) {
			throw new BizException("请选择结算账户");
		}
		Member member = memberRepo.getOne(param.getMemberId());
		double balance = NumberUtil.round(member.getBalance() - param.getAmount(), 2).doubleValue();
		if (balance < 0) {
			throw new BizException("余额不足");
		}

		WithdrawRecord record = param.convertToPo(settlementAccount, 0d);
		withdrawRecordRepo.save(record);

		member.setBalance(balance);
		memberRepo.save(member);

		memberBalanceChangeLogRepo.save(MemberBalanceChangeLog.buildWithWithdraw(member, record));
	}

}
