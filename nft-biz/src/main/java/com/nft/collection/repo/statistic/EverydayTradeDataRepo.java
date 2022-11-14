package com.nft.collection.repo.statistic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.nft.collection.domain.statistic.EverydayTradeData;

public interface EverydayTradeDataRepo
		extends JpaRepository<EverydayTradeData, String>, JpaSpecificationExecutor<EverydayTradeData> {

}
