package com.nft.member.repo;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.nft.member.domain.WithdrawRecord;

public interface WithdrawRecordRepo extends JpaRepository<WithdrawRecord, String>,
		JpaSpecificationExecutor<WithdrawRecord> {
	
	@Modifying
	@Query(nativeQuery = true, value = "delete from member_withdraw_record where submit_time >= ?1 and submit_time <= ?2 and withdraw_fund_type = ?3")
	Integer dataClean(Date startTime, Date endTime, String withdrawFundType);

}
