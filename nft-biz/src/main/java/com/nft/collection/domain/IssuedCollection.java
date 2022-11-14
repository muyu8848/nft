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

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "issued_collection")
@DynamicInsert(true)
@DynamicUpdate(true)
public class IssuedCollection implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", length = 32)
	private String id;

	private Integer collectionSerialNumber;

	private Date issueTime;

	private String uniqueId;

	private Date syncChainTime;
	
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
	
	public void deleted() {
		this.setDeletedFlag(true);
		this.setDeletedTime(new Date());
	}

	public void syncChain(String uniqueId) {
		this.setUniqueId(uniqueId);
		this.setSyncChainTime(new Date());
	}

	public MemberHoldCollection firstIssueToMember(String memberId, Double price, String gainWay) {
		MemberHoldCollection po = new MemberHoldCollection();
		po.setId(IdUtils.getId());
		po.setGainWay(gainWay);
		po.setState(Constant.持有藏品状态_持有中);
		po.setHoldTime(new Date());
		po.setPrice(price);
		po.setCollectionId(this.getCollectionId());
		po.setIssuedCollectionId(this.getId());
		po.setMemberId(memberId);
		return po;
	}

}
