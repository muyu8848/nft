package com.nft.collection.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.nft.collection.domain.ComposeActivity;

public interface ComposeActivityRepo
		extends JpaRepository<ComposeActivity, String>, JpaSpecificationExecutor<ComposeActivity> {

	List<ComposeActivity> findByActivityTimeStartLessThanEqualAndActivityTimeEndGreaterThanEqualAndDeletedFlagFalseOrderByActivityTimeEndAsc(
			Date d1, Date d2);

}
