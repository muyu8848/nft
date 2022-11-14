package com.nft.chain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nft.setting.repo.ChainSettingRepo;

import cn.hutool.extra.spring.SpringUtil;

@Service
public class ChainService {

	@Autowired
	private ChainSettingRepo chainSettingRepo;

	@Transactional
	public ChainAbstractService getChainServiceImpl() {
		String currentInUseChain = chainSettingRepo.findTopByOrderByLatelyUpdateTime().getCurrentInUseChain();
		ChainAbstractService chainAbstractService = SpringUtil.getBean(currentInUseChain + "Service",
				ChainAbstractService.class);
		return chainAbstractService;
	}

	@Transactional
	public String syncTransactionHash(String id) {
		return getChainServiceImpl().syncTransactionHash(id);
	}

	@Transactional
	public String syncUniqueId(String id) {
		return getChainServiceImpl().syncUniqueId(id);
	}

	@Transactional
	public void transferArtwork(String id) {
		getChainServiceImpl().chainTransfer(id);
	}

	@Transactional
	public void marketBuyArtwork(String id) {
		getChainServiceImpl().chainTransfer(id);
	}

	@Transactional
	public String chainTransfer(String id) {
		return getChainServiceImpl().chainTransfer(id);
	}

	@Transactional
	public void destroyArtwork(String id) {
		getChainServiceImpl().destroyArtwork(id);
	}

	@Transactional
	public String mintArtwork(String id) {
		return getChainServiceImpl().mintArtwork(id);
	}

	@Transactional
	public String createBlockChainAddr(String id) {
		return getChainServiceImpl().createBlockChainAddr(id);
	}

	@Transactional
	public String syncArtworkHash(String id) {
		return getChainServiceImpl().syncArtworkHash(id);
	}

	@Transactional
	public String chainAddArtwork(String id) {
		return getChainServiceImpl().chainAddArtwork(id);
	}

}
