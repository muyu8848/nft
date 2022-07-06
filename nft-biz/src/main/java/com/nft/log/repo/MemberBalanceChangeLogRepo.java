package com.nft.log.repo;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.nft.log.domain.MemberBalanceChangeLog;

public interface MemberBalanceChangeLogRepo
		extends JpaRepository<MemberBalanceChangeLog, String>, JpaSpecificationExecutor<MemberBalanceChangeLog> {

	@Modifying
	@Query(nativeQuery = true, value = "delete from member_balance_change_log where change_time >= ?1 and change_time <= ?2")
	Integer dataClean(Date startTime, Date endTime);
	
}
