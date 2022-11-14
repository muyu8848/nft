package com.nft.collection.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.nft.collection.domain.Collection;

public interface CollectionRepo extends JpaRepository<Collection, String>, JpaSpecificationExecutor<Collection> {

	List<Collection> findByDeletedFlagFalseOrderByCreateTimeDesc();

}
