package com.nft.collection.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.nft.collection.domain.PayOrder;

public interface PayOrderRepo extends JpaRepository<PayOrder, String>, JpaSpecificationExecutor<PayOrder> {

}
