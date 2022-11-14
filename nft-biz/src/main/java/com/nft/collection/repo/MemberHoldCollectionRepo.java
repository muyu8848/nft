package com.nft.collection.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.nft.collection.domain.MemberHoldCollection;

public interface MemberHoldCollectionRepo
		extends JpaRepository<MemberHoldCollection, String>, JpaSpecificationExecutor<MemberHoldCollection> {

	List<MemberHoldCollection> findByMemberIdAndStateAndIdIn(String memberId, String state, List<String> ids);

}
