package com.nft.sms.repo;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.nft.sms.domain.SmsSendRecord;

public interface SmsSendRecordRepo
		extends JpaRepository<SmsSendRecord, String>, JpaSpecificationExecutor<SmsSendRecord> {

	@Modifying
	@Query(nativeQuery = true, value = "delete from sms_send_record where create_time >= ?1 and create_time <= ?2")
	Integer dataClean(Date startTime, Date endTime);

}
