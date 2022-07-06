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
import com.nft.member.domain.Member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "collection_give_record")
@DynamicInsert(true)
@DynamicUpdate(true)
public class CollectionGiveRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", length = 32)
	private String id;
	
	private String orderNo;

	private Date giveTime;

	@Version
	private Long version;

	@Column(name = "hold_collection_id", length = 32)
	private String holdCollectionId;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hold_collection_id", updatable = false, insertable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private MemberHoldCollection holdCollection;

	@Column(name = "give_from_id", length = 32)
	private String giveFromId;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "give_from_id", updatable = false, insertable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Member giveFrom;

	@Column(name = "give_to_id", length = 32)
	private String giveToId;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "give_to_id", updatable = false, insertable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Member giveTo;

	public static CollectionGiveRecord build(String holdCollectionId, String giveFromId, String giveToId) {
		CollectionGiveRecord po = new CollectionGiveRecord();
		po.setId(IdUtils.getId());
		po.setOrderNo(po.getId());
		po.setGiveTime(new Date());
		po.setHoldCollectionId(holdCollectionId);
		po.setGiveFromId(giveFromId);
		po.setGiveToId(giveToId);
		return po;
	}

}
