package com.nft.collection.param;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.nft.collection.domain.PreSaleQualify;
import com.nft.common.param.PageParam;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PreSaleQualifyQueryCondParam extends PageParam {

	private String bizType;

	private String preSaleTaskId;

	private String memberMobile;

	private String collectionName;

	private String state;

	public Specification<PreSaleQualify> buildSpecification() {
		PreSaleQualifyQueryCondParam param = this;
		Specification<PreSaleQualify> spec = new Specification<PreSaleQualify>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<PreSaleQualify> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (StrUtil.isNotEmpty(param.getBizType())) {
					predicates.add(builder.equal(root.get("bizType"), param.getBizType()));
				}
				if (StrUtil.isNotEmpty(param.getPreSaleTaskId())) {
					predicates.add(builder.equal(root.get("preSaleTaskId"), param.getPreSaleTaskId()));
				}
				if (StrUtil.isNotEmpty(param.getMemberMobile())) {
					predicates.add(builder.equal(root.join("member").get("mobile"), param.getMemberMobile()));
				}
				if (StrUtil.isNotEmpty(param.getCollectionName())) {
					predicates.add(builder.equal(root.join("collection").get("name"), param.getCollectionName()));
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
