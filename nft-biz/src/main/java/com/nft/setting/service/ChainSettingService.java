package com.nft.setting.service;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.nft.setting.domain.ChainSetting;
import com.nft.setting.repo.ChainSettingRepo;

@Validated
@Service
public class ChainSettingService {

	@Autowired
	private ChainSettingRepo chainSettingRepo;

	@Transactional(readOnly = true)
	public String getCurrentInUseChain() {
		ChainSetting setting = chainSettingRepo.findTopByOrderByLatelyUpdateTime();
		return setting != null ? setting.getCurrentInUseChain() : "";
	}

	@Transactional
	public void updateCurrentInUseChain(@NotBlank String currentInUseChain) {
		ChainSetting setting = chainSettingRepo.findTopByOrderByLatelyUpdateTime();
		if (setting == null) {
			setting = ChainSetting.build();
		}
		setting.setCurrentInUseChain(currentInUseChain);
		setting.setLatelyUpdateTime(new Date());
		chainSettingRepo.save(setting);
	}

}
