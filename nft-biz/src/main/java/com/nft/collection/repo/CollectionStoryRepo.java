package com.nft.collection.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.nft.collection.domain.CollectionStory;

public interface CollectionStoryRepo
		extends JpaRepository<CollectionStory, String>, JpaSpecificationExecutor<CollectionStory> {

	List<CollectionStory> findByCollectionIdOrderByOrderNo(String collectionId);

}
