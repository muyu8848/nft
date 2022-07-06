package com.nft.storage.vo;

import java.io.Serializable;
import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nft.storage.domain.Storage;

import lombok.Data;

@Data
public class StorageVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private String fileName;

	private String fileType;

	private Long fileSize;

	private String url;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date uploadTime;

	private String associateBiz;

	private String associateId;

	public static StorageVO convertFor(Storage storage) {
		if (storage == null) {
			return null;
		}
		StorageVO vo = new StorageVO();
		BeanUtils.copyProperties(storage, vo);
		return vo;
	}

}
