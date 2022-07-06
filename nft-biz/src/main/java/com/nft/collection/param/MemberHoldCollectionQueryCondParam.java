package com.nft.collection.param;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.nft.collection.domain.MemberHoldCollection;
import com.nft.common.param.PageParam;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MemberHoldCollectionQueryCondParam extends PageParam {

	private String memberMobile;

	private String collectionName;
	
	private String gainWay;

	private String memberId;
	
	private String state;

	public Specification<MemberHoldCollection> buildSpecification() {
		MemberHoldCollectionQueryCondParam param = this;
		Specification<MemberHoldCollection> spec = new Specification<MemberHoldCollection>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<MemberHoldCollection> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (StrUtil.isNotEmpty(param.getMemberMobile())) {
					predicates.add(builder.equal(root.join("member").get("mobile"), param.getMemberMobile()));
				}
				if (StrUtil.isNotEmpty(param.getCollectionName())) {
					predicates.add(builder.equal(root.join("collection").get("name"), param.getCollectionName()));
				}
				if (StrUtil.isNotEmpty(param.getGainWay())) {
					predicates.add(builder.equal(root.get("gainWay"), param.getGainWay()));
				}
				if (StrUtil.isNotEmpty(param.getMemberId())) {
					predicates.add(builder.equal(root.get("memberId"), param.getMemberId()));
				}
				if (StrUtil.isNotEmpty(param.getState())) {
					predicates.add(builder.equal(root.get("state"), param.getState()));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		return spec;
	}

}