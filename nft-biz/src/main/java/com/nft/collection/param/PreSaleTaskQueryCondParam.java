package com.nft.collection.param;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.nft.collection.domain.PreSaleTask;
import com.nft.common.param.PageParam;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PreSaleTaskQueryCondParam extends PageParam {

	private String taskName;

	private String collectionName;

	private String state;

	public Specification<PreSaleTask> buildSpecification() {
		PreSaleTaskQueryCondParam param = this;
		Specification<PreSaleTask> spec = new Specification<PreSaleTask>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<PreSaleTask> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (StrUtil.isNotEmpty(param.getTaskName())) {
					predicates.add(builder.equal(root.get("taskName"), param.getTaskName()));
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
