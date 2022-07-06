package com.nft.collection.param;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.nft.collection.domain.CollectionGiveRecord;
import com.nft.common.param.PageParam;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CollectionGiveRecordQueryCondParam extends PageParam {

	private String collectionName;

	private String memberId;

	private String giveDirection;

	public Specification<CollectionGiveRecord> buildSpecification() {
		CollectionGiveRecordQueryCondParam param = this;
		Specification<CollectionGiveRecord> spec = new Specification<CollectionGiveRecord>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<CollectionGiveRecord> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (StrUtil.isNotEmpty(param.getCollectionName())) {
					predicates.add(builder.equal(root.join("holdCollection").join("collection").get("name"),
							param.getCollectionName()));
				}
				if (StrUtil.isNotEmpty(param.getMemberId())) {
					Predicate giveFromId = builder.equal(root.get("giveFromId"), param.getMemberId());
					Predicate giveToId = builder.equal(root.get("giveToId"), param.getMemberId());
					if (StrUtil.isBlank(param.getGiveDirection())) {
						Predicate or = builder.or(giveFromId, giveToId);
						Predicate and = builder.and(or);
						predicates.add(and);
					} else if ("from".equals(param.getGiveDirection())) {
						predicates.add(giveFromId);
					} else if ("to".equals(param.getGiveDirection())) {
						predicates.add(giveToId);
					}

				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		return spec;
	}

}
