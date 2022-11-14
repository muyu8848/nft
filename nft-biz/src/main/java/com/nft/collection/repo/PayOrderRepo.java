package com.nft.collection.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.nft.collection.domain.PayOrder;

public interface PayOrderRepo extends JpaRepository<PayOrder, String>, JpaSpecificationExecutor<PayOrder> {

	PayOrder findTopByMemberIdAndCollectionIdAndState(String memberId, String collectionId, String state);

	PayOrder findTopByMemberIdAndCollectionIdAndBizOrderNoAndState(String memberId, String collectionId,
			String bizOrderNo, String state);

	List<PayOrder> findByStateAndOrderDeadlineLessThan(String state, Date orderDeadline);

}
