package com.nft.collection.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.nft.collection.domain.MemberHoldCollection;

public interface MemberHoldCollectionRepo
		extends JpaRepository<MemberHoldCollection, String>, JpaSpecificationExecutor<MemberHoldCollection> {

}
