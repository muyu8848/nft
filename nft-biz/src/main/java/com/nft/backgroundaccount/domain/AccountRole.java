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
@Table(name = "account_role")
@DynamicInsert(true)
@DynamicUpdate(true)
public class AccountRole implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", length = 32)
	private String id;

	@Column(name = "account_id", length = 32)
	private String accountId;

	@Column(name = "role_id", length = 32)
	private String roleId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account_id", updatable = false, insertable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private BackgroundAccount backgroundAccount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", updatable = false, insertable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Role role;

	public static AccountRole build(String accountId, String roleId) {
		AccountRole accountRole = new AccountRole();
		accountRole.setId(IdUtils.getId());
		accountRole.setAccountId(accountId);
		accountRole.setRoleId(roleId);
		return accountRole;
	}

}
