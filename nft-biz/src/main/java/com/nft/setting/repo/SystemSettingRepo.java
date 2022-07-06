package com.nft.setting.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.nft.setting.domain.SystemSetting;

public interface SystemSettingRepo
		extends JpaRepository<SystemSetting, String>, JpaSpecificationExecutor<SystemSetting> {

	SystemSetting findTopByOrderByLatelyUpdateTime();

}
