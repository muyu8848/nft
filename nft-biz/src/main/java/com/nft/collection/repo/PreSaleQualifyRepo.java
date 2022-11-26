package com.nft.collection.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.nft.collection.domain.PreSaleQualify;

public interface PreSaleQualifyRepo
		extends JpaRepository<PreSaleQualify, String>, JpaSpecificationExecutor<PreSaleQualify> {

	PreSaleQualify findTopByMemberIdAndCollectionIdAndStateOrderByPreMinuteDesc(String memberId, String collectionId, String state);

}
