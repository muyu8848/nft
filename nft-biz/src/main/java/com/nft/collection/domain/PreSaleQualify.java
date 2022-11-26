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

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pre_sale_qualify")
@DynamicInsert(true)
@DynamicUpdate(true)
public class PreSaleQualify implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", length = 32)
	private String id;
	
	private String bizType;

	private Integer preMinute;

	private Date grantTime;

	private Date dealTime;

	private String state;

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
	
	@Column(name = "pre_sale_task_id", length = 32)
	private String preSaleTaskId;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pre_sale_task_id", updatable = false, insertable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private PreSaleTask preSaleTask;

	public static PreSaleQualify buildWithPreSaleTask(String memberId, PreSaleTask preSaleTask) {
		PreSaleQualify po = new PreSaleQualify();
		po.setId(IdUtils.getId());
		po.setBizType(Constant.优先购业务类型_优先购任务);
		po.setState(Constant.优先购资格状态_未使用);
		po.setPreMinute(preSaleTask.getPreMinute());
		po.setMemberId(memberId);
		po.setCollectionId(preSaleTask.getCollectionId());
		po.setPreSaleTaskId(preSaleTask.getId());
		return po;
	}

	public void used(String issuedCollectionId) {
		this.setIssuedCollectionId(issuedCollectionId);
		this.setDealTime(new Date());
		this.setState(Constant.优先购资格状态_已使用);
	}

}
