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
import org.springframework.beans.BeanUtils;

import com.nft.common.utils.IdUtils;
import com.nft.constants.Constant;
import com.nft.member.domain.Member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "member_hold_collection")
@DynamicInsert(true)
@DynamicUpdate(true)
public class MemberHoldCollection implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", length = 32)
	private String id;

	private Double price;

	private String gainWay;

	private String state;

	private Date holdTime;

	private Date loseTime;

	private Date resaleTime;

	private Double resalePrice;

	@Version
	private Long version;

	@Column(name = "member_id", length = 32)
	private String memberId;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", updatable = false, insertable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Member member;

	@Column(name = "collection_id", length = 32)
	private String collectionId;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "collection_id", updatable = false, insertable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Collection collection;
	
	public void cancelResale() {
		this.setState(Constant.持有藏品状态_持有中);
		this.setResaleTime(null);
		this.setResalePrice(null);
	}

	public void resale(Double resalePrice) {
		this.setState(Constant.持有藏品状态_转售中);
		this.setResaleTime(new Date());
		this.setResalePrice(resalePrice);
	}

	public MemberHoldCollection buildWithGive(String giveToId) {
		MemberHoldCollection po = new MemberHoldCollection();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setHoldTime(new Date());
		po.setMemberId(giveToId);
		po.setGainWay(Constant.藏品获取方式_赠送);
		return po;
	}

	public MemberHoldCollection buildWithResale(String buyerId) {
		MemberHoldCollection po = new MemberHoldCollection();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setHoldTime(new Date());
		po.setLoseTime(null);
		po.setResaleTime(null);
		po.setResalePrice(null);
		po.setGainWay(Constant.藏品获取方式_二级市场);
		po.setMemberId(buyerId);
		po.setPrice(this.getResalePrice());
		po.setState(Constant.持有藏品状态_持有中);
		return po;
	}

}
