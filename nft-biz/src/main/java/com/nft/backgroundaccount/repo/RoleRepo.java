package com.nft.backgroundaccount.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.nft.backgroundaccount.domain.Role;

public interface RoleRepo extends JpaRepository<Role, String>, JpaSpecificationExecutor<Role> {
	
	List<Role> findByDeletedFlagFalse();

}
