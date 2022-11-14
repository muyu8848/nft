package com.nft.collection.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.nft.collection.domain.ComposeMaterial;

public interface ComposeMaterialRepo
		extends JpaRepository<ComposeMaterial, String>, JpaSpecificationExecutor<ComposeMaterial> {
	
	List<ComposeMaterial> findByActivityId(String activityId);

}
