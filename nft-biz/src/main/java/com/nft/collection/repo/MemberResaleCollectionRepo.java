package com.nft.collection.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.nft.collection.domain.MemberResaleCollection;

public interface MemberResaleCollectionRepo
		extends JpaRepository<MemberResaleCollection, String>, JpaSpecificationExecutor<MemberResaleCollection> {

}
