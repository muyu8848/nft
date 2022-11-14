package com.nft.collection.param;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.nft.collection.domain.ComposeRecord;
import com.nft.common.param.PageParam;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ComposeRecordQueryCondParam extends PageParam {

	private String composeActivityId;

	private String memberMobile;

	public Specification<ComposeRecord> buildSpecification() {
		ComposeRecordQueryCondParam param = this;
		Specification<ComposeRecord> spec = new Specification<ComposeRecord>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<ComposeRecord> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (StrUtil.isNotEmpty(param.getComposeActivityId())) {
					predicates.add(builder.equal(root.get("composeActivityId"), param.getComposeActivityId()));
				}
				if (StrUtil.isNotEmpty(param.getMemberMobile())) {
					predicates.add(builder.equal(root.join("member").get("mobile"), param.getMemberMobile()));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		return spec;
	}

}
