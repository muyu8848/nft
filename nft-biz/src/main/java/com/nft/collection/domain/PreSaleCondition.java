package com.nft.collection.domain;

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
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pre_sale_condition")
@DynamicInsert(true)
@DynamicUpdate(true)
public class PreSaleCondition implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", length = 32)
	private String id;

	private String fieldName;

	private String cond;

	private String fieldValue;

	private String logicalOperation;
	
	private Double orderNo;

	@Column(name = "pre_sale_task_id", length = 32)
	private String preSaleTaskId;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pre_sale_task_id", updatable = false, insertable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private PreSaleTask preSaleTask;

}
