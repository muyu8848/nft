package com.nft.collection.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.nft.collection.domain.AirDropRecord;

public interface AirDropRecordRepo
		extends JpaRepository<AirDropRecord, String>, JpaSpecificationExecutor<AirDropRecord> {

}
