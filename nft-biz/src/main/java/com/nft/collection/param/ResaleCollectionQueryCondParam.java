package com.nft.collection.param;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.nft.collection.domain.MemberResaleCollection;
import com.nft.common.param.PageParam;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ResaleCollectionQueryCondParam extends PageParam {

	private String memberId;

	private String state;

	private String collectionName;

	private String collectionId;

	private String creatorId;

	private String commodityType;

	public Specification<MemberResaleCollection> buildSpecification() {
		ResaleCollectionQueryCondParam param = this;
		Specification<MemberResaleCollection> spec = new Specification<MemberResaleCollection>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<MemberResaleCollection> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (StrUtil.isNotEmpty(param.getCollectionName())) {
					predicates.add(builder.equal(root.join("collection").get("name"), param.getCollectionName()));
				}
				if (StrUtil.isNotEmpty(param.getCollectionId())) {
					predicates.add(builder.equal(root.get("collectionId"), param.getCollectionId()));
				}
				if (StrUtil.isNotEmpty(param.getCreatorId())) {
					predicates.add(builder.equal(root.join("collection").get("creatorId"), param.getCreatorId()));
				}
				if (StrUtil.isNotEmpty(param.getMemberId())) {
					predicates.add(builder.equal(root.get("memberId"), param.getMemberId()));
				}
				if (StrUtil.isNotEmpty(param.getState())) {
					predicates.add(builder.equal(root.get("state"), param.getState()));
				}
				if (StrUtil.isNotEmpty(param.getCommodityType())) {
					predicates
							.add(builder.equal(root.join("collection").get("commodityType"), param.getCommodityType()));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		return spec;
	}

}
