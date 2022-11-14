package com.nft.member.param;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.nft.common.param.PageParam;
import com.nft.member.domain.Member;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MemberQueryCondParam extends PageParam {

	private String mobile;

	private String inviterId;

	private String inviterMobile;

	public Specification<Member> buildSpecification() {
		MemberQueryCondParam param = this;
		Specification<Member> spec = new Specification<Member>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<Member> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				predicates.add(builder.equal(root.get("deletedFlag"), false));
				if (StrUtil.isNotEmpty(param.getMobile())) {
					predicates.add(builder.equal(root.get("mobile"), param.getMobile()));
				}
				if (StrUtil.isNotEmpty(param.getInviterId())) {
					predicates.add(builder.equal(root.get("inviterId"), param.getInviterId()));
				}
				if (StrUtil.isNotEmpty(param.getInviterMobile())) {
					predicates.add(builder.equal(root.join("inviter").get("mobile"), param.getInviterMobile()));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		return spec;
	}

}
