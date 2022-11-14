package com.nft.collection.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.nft.collection.domain.IssuedCollectionActionLog;

public interface IssuedCollectionActionLogRepo
		extends JpaRepository<IssuedCollectionActionLog, String>, JpaSpecificationExecutor<IssuedCollectionActionLog> {

	List<IssuedCollectionActionLog> findByIssuedCollectionIdOrderByActionTimeDesc(String issuedCollectionId);

}
