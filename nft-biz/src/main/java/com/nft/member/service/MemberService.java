package com.nft.member.service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.nft.common.exception.BizException;
import com.nft.common.utils.ThreadPoolUtils;
import com.nft.common.vo.PageResult;
import com.nft.constants.Constant;
import com.nft.log.domain.MemberBalanceChangeLog;
import com.nft.log.repo.MemberBalanceChangeLogRepo;
import com.nft.member.domain.Member;
import com.nft.member.param.AddMemberParam;
import com.nft.member.param.BindRealNameParam;
import com.nft.member.param.MemberQueryCondParam;
import com.nft.member.param.ModifyPayPwdParam;
import com.nft.member.param.UpdateMemberParam;
import com.nft.member.repo.MemberRepo;
import com.nft.member.vo.AccountAuthInfoVO;
import com.nft.member.vo.InviteInfoVO;
import com.nft.member.vo.MemberFundInfoVO;
import com.nft.member.vo.MemberStatisticDataVO;
import com.nft.member.vo.MemberVO;
import com.nft.member.vo.MyInviteRecordVO;
import com.nft.member.vo.MyPersonalInfoVO;
import com.nft.setting.repo.SystemSettingRepo;
import com.nft.sms.domain.SmsSendRecord;
import com.nft.sms.repo.SmsSendRecordRepo;
import com.zengtengpeng.annotation.Lock;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;

@Validated
@Service
public class MemberService {

	@Autowired
	private RedissonClient redissonClient;

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	private MemberRepo memberRepo;

	@Autowired
	private MemberBalanceChangeLogRepo memberBalanceChangeLogRepo;

	@Autowired
	private SmsSendRecordRepo smsSendRecordRepo;

	@Autowired
	private SystemSettingRepo systemSettingRepo;

	@Transactional(readOnly = true)
	public InviteInfoVO getInviteInfo(@NotBlank String memberId) {
		String h5Gateway = systemSettingRepo.findTopByOrderByLatelyUpdateTime().getH5Gateway();
		Member member = memberRepo.getOne(memberId);
		return InviteInfoVO.build(member.getInviteCode(), h5Gateway);
	}

	@Lock(keys = "'sendModifyPayPwdVerificationCode' + #memberId")
	@Transactional
	public void sendModifyPayPwdVerificationCode(@NotBlank String memberId) {
		Member member = memberRepo.getOne(memberId);
		sendVerificationCode(member.getMobile(), Constant.短信类型_验证码_修改支付密码);
	}

	@Lock(keys = "'sendLoginVerificationCode' + #mobile")
	@Transactional
	public void sendLoginVerificationCode(@NotBlank String mobile) {
		sendVerificationCode(mobile, Constant.短信类型_验证码_登录);
	}

	@Lock(keys = "'registerAccount' + #mobile")
	@Transactional
	public void registerAccount(@NotBlank String mobile, String inviteCode) {
		Member newAccount = memberRepo.findByMobileAndDeletedFlagIsFalse(mobile);
		if (newAccount == null) {
			newAccount = Member.build("藏家_" + RandomUtil.randomString(9), mobile);
			if (StrUtil.isNotBlank(inviteCode)) {
				Member inviter = memberRepo.findByInviteCodeAndDeletedFlagIsFalse(inviteCode);
				if (inviter != null) {
					newAccount.updateInviteInfo(inviter);
				}
			}
			memberRepo.save(newAccount);

			addBalance(newAccount.getId(), 5000d, null);
		}
	}

	@Transactional
	public void sendVerificationCode(@NotBlank String mobile, @NotBlank String type) {
		String smsCodeGet = redisTemplate.opsForValue().get(Constant.限制 + type + mobile);
		if (StrUtil.isNotBlank(smsCodeGet)) {
			throw new BizException("请不要频繁获取验证码");
		}

		redisTemplate.opsForValue().set(Constant.限制 + type + mobile, "1", 60, TimeUnit.SECONDS);
		String verificationCode = RandomUtil.randomNumbers(4);
		verificationCode = "6666";
		redisTemplate.opsForValue().set(type + mobile, verificationCode, 60 * 5, TimeUnit.SECONDS);

		SmsSendRecord smsSendRecord = SmsSendRecord.build(mobile, type, verificationCode);
		smsSendRecordRepo.save(smsSendRecord);

		ThreadPoolUtils.getSendSmsPool().schedule(() -> {
			redissonClient.getTopic(Constant.发送短信).publish(smsSendRecord.getId());
		}, 1, TimeUnit.SECONDS);
	}

	@Transactional(readOnly = true)
	public MemberStatisticDataVO getMemberStatisticData() {
		List<Member> result = memberRepo.findAll(new MemberQueryCondParam().buildSpecification(),
				Sort.by(Sort.Order.desc("registeredTime")));
		MemberStatisticDataVO vo = new MemberStatisticDataVO();
		String today = DateUtil.today();
		for (Member data : result) {
			String createDate = DateUtil.format(data.getRegisteredTime(), DatePattern.NORM_DATE_PATTERN);
			if (createDate.equals(today)) {
				vo.setTodayRegisterCount(vo.getTodayRegisterCount() + 1);
			}
			if (data.getBindRealNameTime() != null) {
				vo.setRealNameCount(vo.getRealNameCount() + 1);
			}
		}
		vo.setAccountCount(result.size());
		return vo;
	}

	@Transactional(readOnly = true)
	public MemberFundInfoVO getMemberFundInfo() {
		List<Member> result = memberRepo.findAll(new MemberQueryCondParam().buildSpecification(),
				Sort.by(Sort.Order.desc("registeredTime")));
		return MemberFundInfoVO.convertFor(result);
	}

	@Transactional
	public void updateKeepLoginDuration(@NotBlank String memberId,
			@NotNull @DecimalMin(value = "0", inclusive = false) Integer keepLoginDuration) {
		Member member = memberRepo.getOne(memberId);
		member.setKeepLoginDuration(keepLoginDuration);
		memberRepo.save(member);
	}

	@Transactional(readOnly = true)
	public MyPersonalInfoVO getMyPersonalInfo(@NotBlank String memberId) {
		return MyPersonalInfoVO.convertFor(memberRepo.getOne(memberId));
	}

	@Transactional(readOnly = true)
	public Double getBalance(String accountId) {
		Member member = memberRepo.getOne(accountId);
		return member.getBalance();
	}

	@Transactional
	public void bindRealName(@Valid BindRealNameParam param) {
		Member member = memberRepo.getOne(param.getMemberId());
		if (member.getBindRealNameTime() != null) {
			throw new BizException("该账号已完成实名");
		}
		member.bindRealName(param.getRealName(), param.getIdentityCard());
		memberRepo.save(member);

		ThreadPoolUtils.getBlockChainAddrPool().schedule(() -> {
			redissonClient.getTopic(Constant.创建区块链地址).publish(member.getId());
		}, 1, TimeUnit.SECONDS);
	}

	@Transactional
	public void updateNickName(@NotBlank String memberId, @NotBlank String nickName) {
		Member member = memberRepo.getOne(memberId);
		member.setNickName(nickName);
		memberRepo.save(member);
	}

	@Transactional
	public void updateAvatar(@NotBlank String memberId, @NotBlank String avatar) {
		Member member = memberRepo.getOne(memberId);
		member.setAvatar(avatar);
		memberRepo.save(member);
	}

	@Transactional
	public void updateLatelyLoginTime(String id) {
		Member member = memberRepo.getOne(id);
		member.setLatelyLoginTime(new Date());
		memberRepo.save(member);
	}

	@Transactional
	public void updateMember(@Valid UpdateMemberParam param) {
		Member existAccount = memberRepo.findByMobileAndDeletedFlagIsFalse(param.getMobile());
		if (existAccount != null && !existAccount.getId().equals(param.getId())) {
			throw new BizException("手机号已存在");
		}
		Member member = memberRepo.getOne(param.getId());
		BeanUtils.copyProperties(param, member);
		memberRepo.save(member);
	}

	@Transactional(readOnly = true)
	public MemberVO findMemberById(@NotBlank String accountId) {
		Member member = memberRepo.getOne(accountId);
		return MemberVO.convertFor(member);
	}

	@Transactional(readOnly = true)
	public List<MyInviteRecordVO> findMyInviteRecord(@NotBlank String inviterId) {
		MemberQueryCondParam param = new MemberQueryCondParam();
		param.setInviterId(inviterId);
		List<Member> result = memberRepo.findAll(param.buildSpecification(),
				Sort.by(Sort.Order.desc("registeredTime")));
		return MyInviteRecordVO.convertFor(result);
	}

	@Transactional(readOnly = true)
	public PageResult<MemberVO> findMemberByPage(@Valid MemberQueryCondParam param) {
		Page<Member> result = memberRepo.findAll(param.buildSpecification(), PageRequest.of(param.getPageNum() - 1,
				param.getPageSize(), Sort.by(Sort.Order.desc("registeredTime"))));
		PageResult<MemberVO> pageResult = new PageResult<>(MemberVO.convertFor(result.getContent()), param.getPageNum(),
				param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

	@Transactional
	public void modifyPayPwd(@Valid ModifyPayPwdParam param) {
		Member member = memberRepo.getOne(param.getMemberId());
		String verificationCode = redisTemplate.opsForValue().get(Constant.短信类型_验证码_修改支付密码 + member.getMobile());
		if (!param.getVerificationCode().equals(verificationCode)) {
			throw new BizException("验证码不正确");
		}
		modifyPayPwd(param.getMemberId(), param.getNewPwd());
	}

	@Transactional
	public void modifyPayPwd(@NotBlank String accountId, @NotBlank String newPwd) {
		Member member = memberRepo.getOne(accountId);
		member.setPayPwd(SaSecureUtil.sha256(newPwd));
		memberRepo.save(member);
	}

	@Transactional(readOnly = true)
	public AccountAuthInfoVO getAccountAuthInfo(@NotBlank String mobile) {
		return AccountAuthInfoVO.convertFor(memberRepo.findByMobileAndDeletedFlagIsFalse(mobile));
	}

	@Transactional
	public void addMember(@Valid AddMemberParam param) {
		addMemberInner(param.getNickName(), param.getMobile());
	}

	@Lock(keys = "'addMember' + #mobile")
	public void addMemberInner(String nickName, String mobile) {
		Member existAccount = memberRepo.findByMobileAndDeletedFlagIsFalse(mobile);
		if (existAccount != null) {
			throw new BizException("手机号已存在");
		}
		Member newMember = Member.build(nickName, mobile);
		memberRepo.save(newMember);
	}

	@Transactional
	public void delMember(@NotBlank String accountId) {
		Member member = memberRepo.getOne(accountId);
		member.deleted();
		memberRepo.save(member);
	}

	@Transactional
	public void addBalance(@NotBlank String id, @NotNull @DecimalMin(value = "0", inclusive = true) Double amount,
			@NotBlank String backgroundAccountId) {
		Member member = memberRepo.getOne(id);
		double balanceAfter = NumberUtil.round(member.getBalance() + amount, 2).doubleValue();
		member.setBalance(balanceAfter);
		memberRepo.save(member);

		memberBalanceChangeLogRepo.save(MemberBalanceChangeLog.buildWithSystem(member, amount));
	}

	@Transactional
	public void reduceBalance(@NotBlank String id, @NotNull @DecimalMin(value = "0", inclusive = true) Double amount,
			@NotBlank String backgroundAccountId) {
		Member member = memberRepo.getOne(id);
		double balanceAfter = NumberUtil.round(member.getBalance() - amount, 2).doubleValue();
		if (balanceAfter < 0) {
			throw new BizException("余额不能少于0");
		}
		member.setBalance(balanceAfter);
		memberRepo.save(member);

		memberBalanceChangeLogRepo.save(MemberBalanceChangeLog.buildWithSystem(member, -amount));
	}

}
