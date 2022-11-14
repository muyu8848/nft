package com.nft.collection.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.nft.common.utils.IdUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "collection")
@DynamicInsert(true)
@DynamicUpdate(true)
public class Collection implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", length = 32)
	private String id;

	private String commodityType;

	private String name;

	private String cover;

	private String collectionHash;
	
	private Double price;

	private Integer quantity;

	private Integer stock;

	private Date saleTime;

	private Boolean externalSaleFlag;

	private Date createTime;
	
	private Date syncChainTime;

	private Boolean deletedFlag;

	private Date deletedTime;

	@Version
	private Long version;

	@Column(name = "creator_id", length = 32)
	private String creatorId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creator_id", updatable = false, insertable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Creator creator;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "collection_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	@OrderBy("orderNo ASC")
	private Set<CollectionStory> collectionStorys;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "collection_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	@OrderBy("probability ASC")
	private Set<MysteryBoxCommodity> subCommoditys;
	
	public void syncChain(String collectionHash) {
		this.setCollectionHash(collectionHash);
		this.setSyncChainTime(new Date());
	}

	public void deleted() {
		this.setDeletedFlag(true);
		this.setDeletedTime(new Date());
	}

	public IssuedCollection issue() {
		IssuedCollection po = new IssuedCollection();
		po.setId(IdUtils.getId());
		po.setIssueTime(new Date());
		po.setCollectionSerialNumber(this.getQuantity() - this.getStock());
		po.setCollectionId(this.getId());
		po.setDeletedFlag(false);
		return po;
	}

}
