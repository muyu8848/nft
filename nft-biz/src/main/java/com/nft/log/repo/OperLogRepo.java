package com.nft.log.repo;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.nft.log.domain.OperLog;

public interface OperLogRepo extends JpaRepository<OperLog, String>, JpaSpecificationExecutor<OperLog> {
	
	@Modifying
	@Query(nativeQuery = true, value = "delete from oper_log where oper_time >= ?1 and oper_time <= ?2")
	Integer dataClean(Date startTime, Date endTime);

}
