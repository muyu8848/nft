package com.nft.collection.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.nft.collection.domain.ExchangeCode;

public interface ExchangeCodeRepo extends JpaRepository<ExchangeCode, String>, JpaSpecificationExecutor<ExchangeCode> {

	ExchangeCode findTopByCodeAndStateAndDeletedFlagFalse(String code, String state);

}
