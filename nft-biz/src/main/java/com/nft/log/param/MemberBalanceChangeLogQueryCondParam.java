package com.nft.log.param;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;

import com.nft.common.param.PageParam;
import com.nft.log.domain.MemberBalanceChangeLog;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MemberBalanceChangeLogQueryCondParam extends PageParam {

	private String memberId;
	
	private String bizOrderNo;

	private String changeType;
	
	private String mobile;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date timeStart;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date timeEnd;
	
	public Specification<MemberBalanceChangeLog> buildSpecification() {
		MemberBalanceChangeLogQueryCondParam param = this;
		Specification<MemberBalanceChangeLog> spec = new Specification<MemberBalanceChangeLog>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<MemberBalanceChangeLog> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (StrUtil.isNotBlank(param.getMemberId())) {
					predicates.add(builder.equal(root.get("memberId"), param.getMemberId()));
				}
				if (StrUtil.isNotBlank(param.getMobile())) {
					predicates.add(builder.equal(root.join("member").get("mobile"), param.getMobile()));
				}
				if (StrUtil.isNotBlank(param.getBizOrderNo())) {
					predicates.add(builder.equal(root.get("bizOrderNo"), param.getBizOrderNo()));
				}
				if (StrUtil.isNotBlank(param.getChangeType())) {
					predicates.add(builder.equal(root.get("changeType"), param.getChangeType()));
				}
				if (param.getTimeStart() != null) {
					predicates.add(builder.greaterThanOrEqualTo(root.get("changeTime").as(Date.class),
							DateUtil.beginOfDay(param.getTimeStart())));
				}
				if (param.getTimeEnd() != null) {
					predicates.add(builder.lessThanOrEqualTo(root.get("changeTime").as(Date.class),
							DateUtil.endOfDay(param.getTimeEnd())));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		return spec;
	}

}
