package com.nft.dictconfig.domain;

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
import javax.persistence.Version;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "dict_item")
@DynamicInsert(true)
@DynamicUpdate(true)
public class DictItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", length = 32)
	private String id;

	private String dictItemCode;

	private String dictItemName;
	
	private Double orderNo;
	
	@Version
	private Long version;

	@Column(name = "dict_type_id", length = 32)
	private String dictTypeId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dict_type_id", updatable = false, insertable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private DictType dictType;

}
