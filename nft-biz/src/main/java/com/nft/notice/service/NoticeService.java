package com.nft.notice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.nft.common.utils.ThreadPoolUtils;
import com.nft.common.vo.PageResult;
import com.nft.constants.Constant;
import com.nft.member.domain.Member;
import com.nft.member.repo.MemberRepo;
import com.nft.notice.domain.Notice;
import com.nft.notice.param.AddOrUpdateNoticeParam;
import com.nft.notice.param.NoticeQueryCondParam;
import com.nft.notice.repo.NoticeRepo;
import com.nft.notice.vo.NoticeAbstractVO;
import com.nft.notice.vo.NoticeVO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

@Validated
@Service
public class NoticeService {

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	private RedissonClient redissonClient;

	@Autowired
	private NoticeRepo noticeRepo;

	@Autowired
	private MemberRepo memberRepo;

	public void allMarkRead(@NotBlank String memberId) {
		redisTemplate.delete(Constant.未读公告 + memberId);
	}

	public void markRead(@NotBlank String id, @NotBlank String memberId) {
		Boolean unreadFlag = redisTemplate.opsForSet().isMember(Constant.未读公告 + memberId, id);
		if (unreadFlag) {
			redisTemplate.opsForSet().remove(Constant.未读公告 + memberId, id);
		}
	}

	@Transactional(readOnly = true)
	public List<String> findUnreadNoticeId(@NotBlank String memberId) {
		Set<String> redisUnreadNoticeIds = redisTemplate.opsForSet().members(Constant.未读公告 + memberId);
		if (CollUtil.isEmpty(redisUnreadNoticeIds)) {
			return new ArrayList<>();
		}
		List<Notice> notices = noticeRepo.findByIdInAndDeletedFlagIsFalse(new ArrayList<>(redisUnreadNoticeIds));
		List<String> unreadNoticeIds = notices.stream().map(p -> p.getId()).collect(Collectors.toList());
		return unreadNoticeIds;
	}

	@Transactional
	public void unreadNoticeSync(String id) {
		List<Member> members = memberRepo.findByDeletedFlagIsFalse();
		for (Member member : members) {
			redisTemplate.opsForSet().add(Constant.未读公告 + member.getId(), id);
		}
	}

	@Transactional
	public void addOrUpdateNotice(@Valid AddOrUpdateNoticeParam param) {
		// 新增
		if (StrUtil.isBlank(param.getId())) {
			Notice notice = param.convertToPo();
			noticeRepo.save(notice);

			ThreadPoolUtils.getUnreadNoticePool().schedule(() -> {
				redissonClient.getTopic(Constant.未读公告同步).publish(notice.getId());
			}, 1, TimeUnit.SECONDS);
		}
		// 修改
		else {
			Notice notice = noticeRepo.getOne(param.getId());
			BeanUtils.copyProperties(param, notice);
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

	public Specification<Notice> buildQueryCond(NoticeQueryCondParam param) {
		Specification<Notice> spec = new Specification<Notice>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<Notice> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				predicates.add(builder.equal(root.get("deletedFlag"), false));
				if (StrUtil.isNotEmpty(param.getTitle())) {
					predicates.add(builder.like(root.get("title"), "%" + param.getTitle() + "%"));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		return spec;
	}

	@Transactional(readOnly = true)
	public PageResult<NoticeAbstractVO> findNoticeAbstractByPage(@Valid NoticeQueryCondParam param) {
		Specification<Notice> spec = buildQueryCond(param);
		Page<Notice> result = noticeRepo.findAll(spec,
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("publishTime"))));
		PageResult<NoticeAbstractVO> pageResult = new PageResult<>(NoticeAbstractVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

	@Transactional(readOnly = true)
	public PageResult<NoticeVO> findByPage(@Valid NoticeQueryCondParam param) {
		Specification<Notice> spec = buildQueryCond(param);
		Page<Notice> result = noticeRepo.findAll(spec,
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("publishTime"))));
		PageResult<NoticeVO> pageResult = new PageResult<>(NoticeVO.convertFor(result.getContent()), param.getPageNum(),
				param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

}
