package com.nft.notice.service;

import java.util.Date;

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

import com.nft.common.vo.PageResult;
import com.nft.notice.domain.Notice;
import com.nft.notice.param.AddOrUpdateNoticeParam;
import com.nft.notice.param.NoticeQueryCondParam;
import com.nft.notice.repo.NoticeRepo;
import com.nft.notice.vo.NoticeAbstractVO;
import com.nft.notice.vo.NoticeVO;

import cn.hutool.core.util.StrUtil;

@Validated
@Service
public class NoticeService {

	@Autowired
	private NoticeRepo noticeRepo;

	@Transactional
	public void addOrUpdateNotice(@Valid AddOrUpdateNoticeParam param) {
		// 新增
		if (StrUtil.isBlank(param.getId())) {
			Notice notice = param.convertToPo();
			noticeRepo.save(notice);
		}
		// 修改
		else {
			Notice notice = noticeRepo.getOne(param.getId());
			BeanUtils.copyProperties(param, notice);
			notice.setLastModifyTime(new Date());
			noticeRepo.save(notice);
		}
	}

	@Transactional
	public void delById(@NotBlank String id) {
		Notice notice = noticeRepo.getOne(id);
		notice.deleted();
		noticeRepo.save(notice);
	}

	@Transactional(readOnly = true)
	public NoticeVO findById(@NotBlank String id) {
		return NoticeVO.convertFor(noticeRepo.getOne(id));
	}

	@Transactional(readOnly = true)
	public PageResult<NoticeAbstractVO> findPublishedNoticeByPage(@Valid NoticeQueryCondParam param) {
		param.setPublishedFlag(true);
		Page<Notice> result = noticeRepo.findAll(param.buildSpecification(),
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("publishTime"))));
		PageResult<NoticeAbstractVO> pageResult = new PageResult<>(NoticeAbstractVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

	@Transactional(readOnly = true)
	public PageResult<NoticeVO> findByPage(@Valid NoticeQueryCondParam param) {
		Page<Notice> result = noticeRepo.findAll(param.buildSpecification(),
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("publishTime"))));
		PageResult<NoticeVO> pageResult = new PageResult<>(NoticeVO.convertFor(result.getContent()), param.getPageNum(),
				param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

}
