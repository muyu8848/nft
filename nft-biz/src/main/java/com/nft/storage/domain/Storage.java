package com.nft.storage.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "storage")
@DynamicInsert(true)
@DynamicUpdate(true)
public class Storage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", length = 32)
	private String id;

	private String fileName;

	private String fileType;

	private Long fileSize;

	private String url;

	private Date uploadTime;

	private String associateBiz;

	private String associateId;

	@Version
	private Long version;

}
