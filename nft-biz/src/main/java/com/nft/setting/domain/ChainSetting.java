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
@Table(name = "chain_setting")
@DynamicInsert(true)
@DynamicUpdate(true)
public class ChainSetting implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", length = 32)
	private String id;

	private String currentInUseChain;

	private Date latelyUpdateTime;

	public static ChainSetting build() {
		ChainSetting setting = new ChainSetting();
		setting.setId(IdUtils.getId());
		return setting;
	}

}
