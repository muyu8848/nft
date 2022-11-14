package com.nft.collection.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.nft.common.utils.IdUtils;
import com.nft.constants.Constant;
import com.nft.member.domain.Member;

import cn.hutool.core.util.RandomUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "exchange_code")
@DynamicInsert(true)
@DynamicUpdate(true)
public class ExchangeCode implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", length = 32)
	private String id;

	private String code;

	private String state;

	private Date createTime;

	private Date exchangeTime;

	private Boolean deletedFlag;

	private Date deletedTime;

	@Version
	private Long version;

	@Column(name = "collection_id", length = 32)
	private String collectionId;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "collection_id", updatable = false, insertable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Collection collection;

	@Column(name = "issued_collection_id", length = 32)
	private String issuedCollectionId;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "issued_collection_id", updatable = false, insertable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private IssuedCollection issuedCollection;

	@Column(name = "member_id", length = 32)
	private String memberId;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", updatable = false, insertable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Member member;

	public void used(String issuedCollectionId, String memberId) {
		this.setState(Constant.兑换码状态_已兑换);
		this.setExchangeTime(new Date());
		this.setIssuedCollectionId(issuedCollectionId);
		this.setMemberId(memberId);
	}

	public static ExchangeCode build(String collectionId, Date createTime) {
		ExchangeCode po = new ExchangeCode();
		po.setId(IdUtils.getId());
		po.setCode(RandomUtil.randomString(8));
		po.setState(Constant.兑换码状态_未兑换);
		po.setCreateTime(createTime);
		po.setCollectionId(collectionId);
		po.setDeletedFlag(false);
		return po;
	}

}
