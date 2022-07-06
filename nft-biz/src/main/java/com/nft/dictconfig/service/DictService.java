package com.nft.dictconfig.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.alicp.jetcache.anno.Cached;
import com.nft.common.exception.BizException;
import com.nft.common.vo.PageResult;
import com.nft.dictconfig.domain.DictItem;
import com.nft.dictconfig.domain.DictType;
import com.nft.dictconfig.param.AddOrUpdateDictTypeParam;
import com.nft.dictconfig.param.DictDataParam;
import com.nft.dictconfig.param.DictTypeQueryCondParam;
import com.nft.dictconfig.param.UpdateDictDataParam;
import com.nft.dictconfig.repo.DictItemRepo;
import com.nft.dictconfig.repo.DictTypeRepo;
import com.nft.dictconfig.vo.DictItemVO;
import com.nft.dictconfig.vo.DictTypeVO;

import cn.hutool.core.util.StrUtil;

@Validated
@Service
public class DictService {

	@Autowired
	private DictItemRepo dictItemRepo;

	@Autowired
	private DictTypeRepo dictTypeRepo;

	@Transactional
	public void updateDictData(@Valid UpdateDictDataParam param) {
		Set<String> dictItemCodeSet = new HashSet<String>();
		for (DictDataParam dictDataParam : param.getDictDatas()) {
			if (!dictItemCodeSet.add(dictDataParam.getDictItemCode())) {
				throw new BizException("字典项code不能重复");
			}
		}

		List<DictItem> oldDictItems = dictItemRepo.findByDictTypeIdOrderByOrderNo(param.getDictTypeId());
		dictItemRepo.deleteAll(oldDictItems);

		double orderNo = 1;
		for (DictDataParam dictDataParam : param.getDictDatas()) {
			DictItem dictItem = dictDataParam.convertToPo();
			dictItem.setDictTypeId(param.getDictTypeId());
			dictItem.setOrderNo(orderNo);
			dictItemRepo.save(dictItem);
			orderNo++;
		}
		DictType dictType = dictTypeRepo.getOne(param.getDictTypeId());
		dictType.setLastModifyTime(new Date());
		dictTypeRepo.save(dictType);
	}

	@Transactional
	public void delDictType(@NotBlank String id) {
		DictType dictType = dictTypeRepo.getOne(id);
		List<DictItem> dictItems = dictItemRepo.findByDictTypeIdOrderByOrderNo(id);
		dictItemRepo.deleteAll(dictItems);
		dictTypeRepo.delete(dictType);
	}

	@Transactional(readOnly = true)
	public DictTypeVO findDictTypeById(@NotBlank String id) {
		return DictTypeVO.convertFor(dictTypeRepo.getOne(id));
	}

	@Transactional
	public void addOrUpdateDictType(@Valid AddOrUpdateDictTypeParam param) {
		if (StrUtil.isBlank(param.getId())) {
			DictType dictType = param.convertToPo();
			dictTypeRepo.save(dictType);
		} else {
			DictType dictType = dictTypeRepo.getOne(param.getId());
			BeanUtils.copyProperties(param, dictType);
			dictType.setLastModifyTime(new Date());
			dictTypeRepo.save(dictType);
		}
	}

	@Transactional(readOnly = true)
	public PageResult<DictTypeVO> findDictTypeByPage(@Valid DictTypeQueryCondParam param) {
		Specification<DictType> spec = new Specification<DictType>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<DictType> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (StrUtil.isNotBlank(param.getDictTypeCode())) {
					predicates.add(builder.like(root.get("dictTypeCode"), "%" + param.getDictTypeCode() + "%"));
				}
				if (StrUtil.isNotBlank(param.getDictTypeName())) {
					predicates.add(builder.like(root.get("dictTypeName"), "%" + param.getDictTypeName() + "%"));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		Page<DictType> result = dictTypeRepo.findAll(spec, PageRequest.of(param.getPageNum() - 1, param.getPageSize(),
				Sort.by(Sort.Order.desc("lastModifyTime"))));
		PageResult<DictTypeVO> pageResult = new PageResult<>(DictTypeVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

	@Cached(name = "dictItem_", key = "args[0] + '_' +  args[1]", expire = 3600)
	@Transactional(readOnly = true)
	public DictItemVO findDictItemByDictTypeCodeAndDictItemCode(String dictTypeCode, String dictItemCode) {
		return DictItemVO
				.convertFor(dictItemRepo.findByDictTypeDictTypeCodeAndDictItemCode(dictTypeCode, dictItemCode));
	}

	@Cached(name = "dictItems_", key = "args[0]", expire = 3600)
	@Transactional(readOnly = true)
	public List<DictItemVO> findDictItemByDictTypeCode(@NotBlank String dictTypeCode) {
		return DictItemVO.convertFor(dictItemRepo.findByDictTypeDictTypeCodeOrderByOrderNo(dictTypeCode));
	}

	@Transactional(readOnly = true)
	public List<DictItemVO> findDictItemByDictTypeId(@NotBlank String dictTypeId) {
		return DictItemVO.convertFor(dictItemRepo.findByDictTypeIdOrderByOrderNo(dictTypeId));
	}

}
