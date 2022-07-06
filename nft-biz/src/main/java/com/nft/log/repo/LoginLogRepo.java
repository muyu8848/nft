package com.nft.log.repo;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.nft.log.domain.LoginLog;

public interface LoginLogRepo extends JpaRepository<LoginLog, String>, JpaSpecificationExecutor<LoginLog> {
	
	@Modifying
	@Query(nativeQuery = true, value = "delete from login_log where login_time >= ?1 and login_time <= ?2")
	Integer dataClean(Date startTime, Date endTime);

}
