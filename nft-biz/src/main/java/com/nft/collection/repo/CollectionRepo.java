package com.nft.collection.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.nft.collection.domain.Collection;

public interface CollectionRepo
		extends JpaRepository<Collection, String>, JpaSpecificationExecutor<Collection> {

}
