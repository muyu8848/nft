package com.nft.collection.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.nft.collection.domain.CollectionGiveRecord;

public interface CollectionGiveRecordRepo
		extends JpaRepository<CollectionGiveRecord, String>, JpaSpecificationExecutor<CollectionGiveRecord> {

}
