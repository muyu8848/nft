package com.nft.setting.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.nft.setting.domain.ChainSetting;

public interface ChainSettingRepo extends JpaRepository<ChainSetting, String>, JpaSpecificationExecutor<ChainSetting> {

	ChainSetting findTopByOrderByLatelyUpdateTime();

}
