package com.nft.collection.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.nft.collection.domain.Creator;

public interface CreatorRepo extends JpaRepository<Creator, String>, JpaSpecificationExecutor<Creator> {

}
