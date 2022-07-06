package com.nft.sms.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.nft.common.utils.IdUtils;
import com.nft.constants.Constant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sms_send_record")
@DynamicInsert(true)
@DynamicUpdate(true)
public class SmsSendRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", length = 32)
	private String id;

	private Date createTime;

	private Date sendTime;

	private String smsType;

	private String mobile;

	private String verificationCode;

	private String errorMsg;

	private String state;

	@Version
	private Long version;

	public void fail(String errorMsg) {
		this.setState(Constant.短信发送状态_发送失败);
		this.setErrorMsg(errorMsg);
		this.setSendTime(new Date());
	}

	public void success() {
		this.setState(Constant.短信发送状态_发送成功);
		this.setSendTime(new Date());
	}

	public static SmsSendRecord build(String mobile, String smsType, String verificationCode) {
		SmsSendRecord po = new SmsSendRecord();
		po.setId(IdUtils.getId());
		po.setCreateTime(new Date());
		po.setMobile(mobile);
		po.setSmsType(smsType);
		po.setVerificationCode(verificationCode);
		po.setState(Constant.短信发送状态_未发送);
		return po;
	}
}
