package com.nft.setting.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.nft.setting.domain.SystemSetting;
import com.nft.setting.param.SystemSettingParam;
import com.nft.setting.repo.SystemSettingRepo;
import com.nft.setting.vo.AppSchemaVO;
import com.nft.setting.vo.LatestAppInfoVO;
import com.nft.setting.vo.SystemSettingVO;

import lombok.extern.slf4j.Slf4j;

@Validated
@Service
@Slf4j
public class SettingService {

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	private SystemSettingRepo systemSettingRepo;

	@Transactional(readOnly = true)
	public String getH5Gateway() {
		SystemSetting setting = systemSettingRepo.findTopByOrderByLatelyUpdateTime();
		return setting.getH5Gateway();
	}

	@Transactional(readOnly = true)
	public AppSchemaVO getAppSchema() {
		SystemSetting setting = systemSettingRepo.findTopByOrderByLatelyUpdateTime();
		return AppSchemaVO.convertFor(setting);
	}

	@Transactional(readOnly = true)
	public LatestAppInfoVO getLatestAppInfo() {
		SystemSetting setting = systemSettingRepo.findTopByOrderByLatelyUpdateTime();
		return LatestAppInfoVO.convertFor(setting);
	}

	@Transactional(readOnly = true)
	public SystemSettingVO getSystemSetting() {
		SystemSetting setting = systemSettingRepo.findTopByOrderByLatelyUpdateTime();
		return SystemSettingVO.convertFor(setting);
	}

	@Transactional
	public void updateSystemSetting(@Valid SystemSettingParam param) {
		SystemSetting setting = systemSettingRepo.findTopByOrderByLatelyUpdateTime();
		if (setting == null) {
			setting = SystemSetting.build();
		}
		BeanUtils.copyProperties(param, setting);
		setting.setLatelyUpdateTime(new Date());
		systemSettingRepo.save(setting);
	}

	public void refreshCache(@NotEmpty List<String> cacheItems) {
		List<String> deleteSuccessKeys = new ArrayList<>();
		List<String> deleteFailKeys = new ArrayList<>();
		for (String cacheItem : cacheItems) {
			Set<String> keys = redisTemplate.keys(cacheItem);
			for (String key : keys) {
				Boolean flag = redisTemplate.delete(key);
				if (flag) {
					deleteSuccessKeys.add(key);
				} else {
					deleteFailKeys.add(key);
				}
			}
		}
		if (!deleteFailKeys.isEmpty()) {
			log.warn("以下的缓存删除失败:", deleteFailKeys);
		}
	}

}
