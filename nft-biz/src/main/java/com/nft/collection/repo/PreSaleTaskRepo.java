package com.nft.collection.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.nft.collection.domain.PreSaleTask;

public interface PreSaleTaskRepo extends JpaRepository<PreSaleTask, String>, JpaSpecificationExecutor<PreSaleTask> {

	List<PreSaleTask> findByStateAndExecuteTimeLessThan(String state, Date executeTime);

}
