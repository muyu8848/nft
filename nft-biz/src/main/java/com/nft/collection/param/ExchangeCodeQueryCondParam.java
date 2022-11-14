package com.nft.collection.param;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.nft.collection.domain.ExchangeCode;
import com.nft.common.param.PageParam;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ExchangeCodeQueryCondParam extends PageParam {

	private String collectionId;

	public Specification<ExchangeCode> buildSpecification() {
		ExchangeCodeQueryCondParam param = this;
		Specification<ExchangeCode> spec = new Specification<ExchangeCode>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<ExchangeCode> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				predicates.add(builder.equal(root.get("deletedFlag"), false));
				if (StrUtil.isNotEmpty(param.getCollectionId())) {
					predicates.add(builder.equal(root.get("collectionId"), param.getCollectionId()));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		return spec;
	}

}
