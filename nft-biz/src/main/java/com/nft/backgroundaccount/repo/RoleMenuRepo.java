package com.nft.backgroundaccount.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.nft.backgroundaccount.domain.RoleMenu;

public interface RoleMenuRepo extends JpaRepository<RoleMenu, String>, JpaSpecificationExecutor<RoleMenu> {

	List<RoleMenu> findByRoleId(String roleId);
	
	List<RoleMenu> findByRoleIdIn(List<String> roleIds);

}
