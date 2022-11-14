package com.nft.collection.domain.statistic;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "v_everyday_trade_data")
@DynamicInsert(true)
@DynamicUpdate(true)
public class EverydayTradeData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private Date everyday;

	private String bizMode;

	private Double successAmount;

	private Integer successCount;

}
