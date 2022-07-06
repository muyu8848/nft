package com.nft.backgroundaccount.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.nft.common.utils.IdUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "role_menu")
@DynamicInsert(true)
@DynamicUpdate(true)
public class RoleMenu implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@Id
	@Column(name = "id", length = 32)
	private String id;

	@Column(name = "role_id", length = 32)
	private String roleId;

	@Column(name = "menu_id", length = 32)
	private String menuId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", updatable = false, insertable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Role role;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_id", updatable = false, insertable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Menu menu;

	public static RoleMenu build(String roleId, String menuId) {
		RoleMenu roleMenu = new RoleMenu();
		roleMenu.setId(IdUtils.getId());
		roleMenu.setRoleId(roleId);
		roleMenu.setMenuId(menuId);
		return roleMenu;
	}

}
