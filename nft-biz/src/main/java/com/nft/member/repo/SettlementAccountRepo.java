package com.nft.member.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.nft.member.domain.SettlementAccount;

public interface SettlementAccountRepo
		extends JpaRepository<SettlementAccount, String>, JpaSpecificationExecutor<SettlementAccount> {

	List<SettlementAccount> findByMemberIdAndDeletedFlagFalseOrderByActivatedTimeDesc(String memberId);

	List<SettlementAccount> findByMemberIdAndActivatedTrueAndDeletedFlagFalseOrderByActivatedTimeDesc(String memberId);

	SettlementAccount findTopByMemberIdAndTypeAndActivatedTrueAndDeletedFlagIsFalseOrderByActivatedTimeDesc(
			String memberId, String type);

	SettlementAccount findByIdAndMemberIdAndDeletedFlagFalse(String id, String memberId);

}
