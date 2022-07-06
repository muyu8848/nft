package com.nft.setting.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.nft.setting.domain.TradeRiskSetting;

public interface TradeRiskSettingRepo
		extends JpaRepository<TradeRiskSetting, String>, JpaSpecificationExecutor<TradeRiskSetting> {

	TradeRiskSetting findTopByOrderByLatelyUpdateTime();

}
