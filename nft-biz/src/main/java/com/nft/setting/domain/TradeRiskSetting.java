package com.nft.setting.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.nft.common.utils.IdUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "trade_risk_setting")
@DynamicInsert(true)
@DynamicUpdate(true)
public class TradeRiskSetting implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", length = 32)
	private String id;

	private Long buyerCancelTrade;

	private Long sellerRejectOrder;

	private Long buyerUnPaid;

	private Long notMySelfPaid;

	private Long paidSellerUnConfirm;

	private Date latelyUpdateTime;

	public static TradeRiskSetting build() {
		TradeRiskSetting setting = new TradeRiskSetting();
		setting.setId(IdUtils.getId());
		return setting;
	}

}
