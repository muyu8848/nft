package com.nft.log.service;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.nft.common.vo.PageResult;
import com.nft.log.domain.LoginLog;
import com.nft.log.param.LoginLogQueryCondParam;
import com.nft.log.repo.LoginLogRepo;
import com.nft.log.vo.LoginLogVO;

@Validated
@Service
public class LoginLogService {

	@Autowired
	private LoginLogRepo loginLogRepo;

	@Transactional(readOnly = true)
	public PageResult<LoginLogVO> findLoginLogByPage(@Valid LoginLogQueryCondParam param) {
		Page<LoginLog> result = loginLogRepo.findAll(param.buildSpecification(),
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("loginTime"))));
		PageResult<LoginLogVO> pageResult = new PageResult<>(LoginLogVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

	@Transactional
	public void recordLoginLog(LoginLog loginLog) {
		loginLogRepo.save(loginLog);
	}

}
