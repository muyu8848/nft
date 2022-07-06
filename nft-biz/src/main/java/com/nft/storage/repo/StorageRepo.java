package com.nft.storage.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.nft.storage.domain.Storage;

public interface StorageRepo extends JpaRepository<Storage, String>, JpaSpecificationExecutor<Storage> {

}
