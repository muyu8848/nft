package com.nft.member.domain;

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

import com.nft.common.exception.BizException;
import com.nft.common.utils.IdUtils;
import com.nft.constants.Constant;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "member")
@DynamicInsert(true)
@DynamicUpdate(true)
public class Member implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", length = 32)
	private String id;

	private String realName;

	private String identityCard;

	private Date bindRealNameTime;

	private String mobile;

	private String nickName;

	private String inviteCode;

	private String avatar;

	private String blockChainAddr;

	private String payPwd;

	private Double balance;

	private Boolean boughtFlag;

	private String state;

	private Integer keepLoginDuration;

	private Date registeredTime;

	private Date latelyLoginTime;

	private Date syncChainTime;

	private Integer accountLevel;

	private String accountLevelPath;

	private Boolean deletedFlag;

	private Date deletedTime;

	@Version
	private Long version;

	@Column(name = "inviter_id", length = 32)
	private String inviterId;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "inviter_id", updatable = false, insertable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Member inviter;

	public void syncChain(String blockChainAddr) {
		this.setBlockChainAddr(blockChainAddr);
		this.setSyncChainTime(new Date());
	}

	public void updateInviteInfo(Member inviter) {
		this.setInviterId(inviter.getId());
		this.setAccountLevel(inviter.getAccountLevel() + 1);
		this.setAccountLevelPath(inviter.getAccountLevelPath() + "." + this.getId());
	}

	public static Member build(String nickName, String mobile) {
		Member po = new Member();
		po.setId(IdUtils.getId());
		po.setNickName(nickName);
		po.setMobile(mobile);
		po.setState(Constant.功能状态_启用);
		po.setAccountLevel(1);
		po.setAccountLevelPath(po.getId());
		po.setRegisteredTime(new Date());
		po.setBalance(0d);
		po.setKeepLoginDuration(12);
		po.setInviteCode(RandomUtil.randomString(7));
		po.setBoughtFlag(false);
		po.setDeletedFlag(false);
		return po;
	}

	public void validBasicRisk() {
		if (Constant.功能状态_禁用.equals(this.getState())) {
			throw new BizException("账号已被禁用");
		}
		if (this.getBindRealNameTime() == null) {
			throw new BizException("该账号还没实名认证");
		}
		if (StrUtil.isBlank(this.getBlockChainAddr())) {
			throw new BizException("该账号还没有区块链地址");
		}
	}

	public void bindRealName(String realName, String identityCard) {
		this.setRealName(realName);
		this.setIdentityCard(identityCard);
		this.setBindRealNameTime(new Date());
	}

	public void deleted() {
		this.setDeletedFlag(true);
		this.setDeletedTime(new Date());
	}

}
