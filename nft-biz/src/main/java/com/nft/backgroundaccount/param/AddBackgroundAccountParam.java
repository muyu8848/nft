package com.nft.backgroundaccount.param;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.BeanUtils;

import com.nft.backgroundaccount.domain.BackgroundAccount;
import com.nft.common.utils.IdUtils;
import com.nft.constants.Constant;

import lombok.Data;

@Data
public class AddBackgroundAccountParam {

	@NotBlank
	private String userName;

	@NotBlank
	private String loginPwd;

	public BackgroundAccount convertToPo() {
		BackgroundAccount po = new BackgroundAccount();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setState(Constant.功能状态_启用);
		po.setDeletedFlag(false);
		po.setRegisteredTime(new Date());
		po.setSuperAdminFlag(false);
		return po;
	}

}
