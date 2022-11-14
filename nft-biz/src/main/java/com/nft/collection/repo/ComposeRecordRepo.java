package com.nft.collection.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.nft.collection.domain.ComposeRecord;

public interface ComposeRecordRepo
		extends JpaRepository<ComposeRecord, String>, JpaSpecificationExecutor<ComposeRecord> {

}
