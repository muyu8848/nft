package com.nft.collection.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.nft.collection.domain.IssuedCollection;

public interface IssuedCollectionRepo
		extends JpaRepository<IssuedCollection, String>, JpaSpecificationExecutor<IssuedCollection> {

	List<IssuedCollection> findByCollectionIdAndDeletedFlagFalseOrderByCollectionSerialNumberDesc(String collectionId);

}
