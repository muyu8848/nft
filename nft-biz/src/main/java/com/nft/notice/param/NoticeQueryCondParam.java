package com.nft.notice.param;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.nft.common.param.PageParam;
import com.nft.notice.domain.Notice;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class NoticeQueryCondParam extends PageParam {

	private String title;

	private String type;

	private Boolean publishedFlag;

	public Specification<Notice> buildSpecification() {
		NoticeQueryCondParam param = this;
		Specification<Notice> spec = new Specification<Notice>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<Notice> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				predicates.add(builder.equal(root.get("deletedFlag"), false));
				if (StrUtil.isNotEmpty(param.getType())) {
					predicates.add(builder.equal(root.get("type"), param.getType()));
				}
				if (StrUtil.isNotEmpty(param.getTitle())) {
					predicates.add(builder.like(root.get("title"), "%" + param.getTitle() + "%"));
				}
				if (param.getPublishedFlag() != null && param.getPublishedFlag()) {
					predicates.add(builder.lessThanOrEqualTo(root.get("publishTime").as(Date.class), new Date()));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		return spec;
	}

}
