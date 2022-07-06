package com.nft.collection.param;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;

import com.nft.collection.domain.Collection;
import com.nft.common.param.PageParam;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CollectionQueryCondParam extends PageParam {

	private String name;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date saleTimeStart;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date saleTimeEnd;

	public Specification<Collection> buildSpecification() {
		CollectionQueryCondParam param = this;
		Specification<Collection> spec = new Specification<Collection>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<Collection> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				predicates.add(builder.equal(root.get("deletedFlag"), false));
				if (StrUtil.isNotEmpty(param.getName())) {
					predicates.add(builder.equal(root.get("name"), param.getName()));
				}
				if (param.getSaleTimeStart() != null) {
					predicates.add(builder.greaterThanOrEqualTo(root.get("saleTime").as(Date.class),
							DateUtil.beginOfDay(param.getSaleTimeStart())));
				}
				if (param.getSaleTimeEnd() != null) {
					predicates.add(builder.lessThanOrEqualTo(root.get("saleTime").as(Date.class),
							DateUtil.endOfDay(param.getSaleTimeEnd())));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		return spec;
	}

}
