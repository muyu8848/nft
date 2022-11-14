package com.nft.collection.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.nft.collection.domain.MysteryBoxCommodity;

public interface MysteryBoxCommodityRepo
		extends JpaRepository<MysteryBoxCommodity, String>, JpaSpecificationExecutor<MysteryBoxCommodity> {

	List<MysteryBoxCommodity> findByCollectionIdOrderByProbabilityAsc(String collectionId);

}
