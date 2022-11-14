package com.nft.collection.service;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.nft.collection.domain.Creator;
import com.nft.collection.param.AddOrUpdateCreatorParam;
import com.nft.collection.param.CreatorQueryCondParam;
import com.nft.collection.repo.CreatorRepo;
import com.nft.collection.vo.CreatorVO;
import com.nft.common.vo.PageResult;

import cn.hutool.core.util.StrUtil;

@Validated
@Service
public class CreatorService {

	@Autowired
	private CreatorRepo creatorRepo;

	@Transactional
	public void delCreator(@NotBlank String id) {
		Creator creator = creatorRepo.getOne(id);
		creator.deleted();
		creatorRepo.save(creator);
	}

	@Transactional(readOnly = true)
	public CreatorVO findCreatorById(@NotBlank String id) {
		Creator creator = creatorRepo.getOne(id);
		return CreatorVO.convertFor(creator);
	}

	@Transactional(readOnly = true)
	public List<CreatorVO> findAllCreator() {
		List<Creator> result = creatorRepo.findAll(new CreatorQueryCondParam().buildSpecification(),
				Sort.by(Sort.Order.desc("createTime")));
		return CreatorVO.convertFor(result);
	}

	@Transactional(readOnly = true)
	public PageResult<CreatorVO> findCreatorByPage(@Valid CreatorQueryCondParam param) {
		Page<Creator> result = creatorRepo.findAll(param.buildSpecification(),
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("createTime"))));
		PageResult<CreatorVO> pageResult = new PageResult<>(CreatorVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

	@Transactional
	public void addOrUpdateCreator(@Valid AddOrUpdateCreatorParam param) {
		if (StrUtil.isBlank(param.getId())) {
			Creator creator = param.convertToPo();
			creatorRepo.save(creator);
		} else {
			Creator creator = creatorRepo.getOne(param.getId());
			BeanUtils.copyProperties(param, creator);
			creator.setLastModifyTime(new Date());
			creatorRepo.save(creator);
		}
	}

}
