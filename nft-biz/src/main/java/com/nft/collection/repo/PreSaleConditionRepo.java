package com.nft.collection.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.nft.collection.domain.PreSaleCondition;

public interface PreSaleConditionRepo
		extends JpaRepository<PreSaleCondition, String>, JpaSpecificationExecutor<PreSaleCondition> {

}
