package com.nft.backgroundaccount.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.alicp.jetcache.anno.Cached;
import com.nft.backgroundaccount.domain.AccountRole;
import com.nft.backgroundaccount.domain.BackgroundAccount;
import com.nft.backgroundaccount.domain.Menu;
import com.nft.backgroundaccount.domain.Role;
import com.nft.backgroundaccount.domain.RoleMenu;
import com.nft.backgroundaccount.param.AddBackgroundAccountParam;
import com.nft.backgroundaccount.param.AssignMenuParam;
import com.nft.backgroundaccount.param.AssignRoleParam;
import com.nft.backgroundaccount.param.BackgroundAccountEditParam;
import com.nft.backgroundaccount.param.BackgroundAccountQueryCondParam;
import com.nft.backgroundaccount.param.MenuParam;
import com.nft.backgroundaccount.param.RoleParam;
import com.nft.backgroundaccount.repo.AccountRoleRepo;
import com.nft.backgroundaccount.repo.BackgroundAccountRepo;
import com.nft.backgroundaccount.repo.MenuRepo;
import com.nft.backgroundaccount.repo.RoleMenuRepo;
import com.nft.backgroundaccount.repo.RoleRepo;
import com.nft.backgroundaccount.vo.AccountAuthInfoVO;
import com.nft.backgroundaccount.vo.BackgroundAccountVO;
import com.nft.backgroundaccount.vo.MenuVO;
import com.nft.backgroundaccount.vo.RoleVO;
import com.nft.backgroundaccount.vo.SuperAdminVO;
import com.nft.common.exception.BizException;
import com.nft.common.googleauth.GoogleAuthInfoVO;
import com.nft.common.googleauth.GoogleAuthenticator;
import com.nft.common.vo.PageResult;
import com.nft.constants.Constant;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;

@Validated
@Service
public class RbacService {

	@Autowired
	private MenuRepo menuRepo;

	@Autowired
	private RoleRepo roleRepo;

	@Autowired
	private RoleMenuRepo roleMenuRepo;

	@Autowired
	private AccountRoleRepo accountRoleRepo;

	@Autowired
	private BackgroundAccountRepo backgroundAccountRepo;

	@Transactional
	public void modifySuperAdminLoginPwd(@NotBlank String accountId, @NotBlank String newPwd,
			@NotBlank String operatorId) {
		BackgroundAccount superAdmin = backgroundAccountRepo.getOne(operatorId);
		if (!superAdmin.getSuperAdminFlag()) {
			throw new BizException("无权修改超级管理员的密码");
		}
		BackgroundAccount account = backgroundAccountRepo.getOne(accountId);
		account.setLoginPwd(SaSecureUtil.sha256(newPwd));
		backgroundAccountRepo.save(account);
	}

	@Transactional(readOnly = true)
	public List<SuperAdminVO> findSuperAdmin() {
		return SuperAdminVO.convertFor(backgroundAccountRepo.findBySuperAdminFlagTrueAndDeletedFlagIsFalse());
	}

	@Transactional
	public void bindGoogleAuth(@NotBlank String id, @NotBlank String googleSecretKey, @NotBlank String googleVerCode) {
		if (!GoogleAuthenticator.checkCode(googleSecretKey, googleVerCode, System.currentTimeMillis())) {
			throw new BizException("谷歌验证码不正确");
		}
		BackgroundAccount account = backgroundAccountRepo.getOne(id);
		if (account.getSuperAdminFlag()) {
			throw new BizException("无权操作超级管理员");
		}
		account.bindGoogleAuth(googleSecretKey);
		backgroundAccountRepo.save(account);
	}

	@Transactional
	public void unBindGoogleAuth(@NotBlank String id) {
		BackgroundAccount account = backgroundAccountRepo.getOne(id);
		account.unBindGoogleAuth();
		backgroundAccountRepo.save(account);
	}

	@Transactional(readOnly = true)
	public GoogleAuthInfoVO getGoogleAuthInfo(@NotBlank String accountId) {
		BackgroundAccount account = backgroundAccountRepo.getOne(accountId);
		return GoogleAuthInfoVO.convertFor(account.getUserName(), account.getGoogleSecretKey(),
				account.getGoogleAuthBindTime());
	}

	@Transactional
	public void updateLatelyLoginTime(String userAccountId) {
		BackgroundAccount userAccount = backgroundAccountRepo.getOne(userAccountId);
		userAccount.setLatelyLoginTime(new Date());
		backgroundAccountRepo.save(userAccount);
	}

	@Transactional
	public void updateBackgroundAccount(@Valid BackgroundAccountEditParam param) {
		BackgroundAccount existAccount = backgroundAccountRepo.findByUserNameAndDeletedFlagIsFalse(param.getUserName());
		if (existAccount != null && !existAccount.getId().equals(param.getId())) {
			throw new BizException("账号已存在");
		}
		if (existAccount != null && existAccount.getSuperAdminFlag()) {
			throw new BizException("无权操作超级管理员");
		}
		BackgroundAccount userAccount = backgroundAccountRepo.getOne(param.getId());
		BeanUtils.copyProperties(param, userAccount);
		backgroundAccountRepo.save(userAccount);
	}

	@Transactional(readOnly = true)
	public PageResult<BackgroundAccountVO> findBackgroundAccountByPage(@Valid BackgroundAccountQueryCondParam param) {
		Specification<BackgroundAccount> spec = new Specification<BackgroundAccount>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<BackgroundAccount> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				predicates.add(builder.equal(root.get("deletedFlag"), false));
				predicates.add(builder.equal(root.get("superAdminFlag"), false));
				if (StrUtil.isNotEmpty(param.getUserName())) {
					predicates.add(builder.like(root.get("userName"), "%" + param.getUserName() + "%"));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		Page<BackgroundAccount> result = backgroundAccountRepo.findAll(spec, PageRequest.of(param.getPageNum() - 1,
				param.getPageSize(), Sort.by(Sort.Order.desc("registeredTime"))));
		PageResult<BackgroundAccountVO> pageResult = new PageResult<>(
				BackgroundAccountVO.convertFor(result.getContent()), param.getPageNum(), param.getPageSize(),
				result.getTotalElements());
		return pageResult;
	}

	@Transactional
	public void modifyLoginPwd(@NotBlank String id, @NotBlank String newPwd) {
		BackgroundAccount account = backgroundAccountRepo.getOne(id);
		if (account.getSuperAdminFlag()) {
			throw new BizException("无权操作超级管理员");
		}
		account.setLoginPwd(SaSecureUtil.sha256(newPwd));
		backgroundAccountRepo.save(account);
	}

	@Transactional(readOnly = true)
	public AccountAuthInfoVO getAccountAuthInfo(@NotBlank String userName) {
		return AccountAuthInfoVO.convertFor(backgroundAccountRepo.findByUserNameAndDeletedFlagIsFalse(userName));
	}

	@Transactional(readOnly = true)
	public BackgroundAccountVO findBackgroundAccountById(@NotBlank String accountId) {
		return BackgroundAccountVO.convertFor(backgroundAccountRepo.getOne(accountId));
	}

	@Transactional
	public void addUserAccount(@Valid AddBackgroundAccountParam param) {
		BackgroundAccount userAccount = backgroundAccountRepo.findByUserNameAndDeletedFlagIsFalse(param.getUserName());
		if (userAccount != null) {
			throw new BizException("账号已存在");
		}
		param.setLoginPwd(SaSecureUtil.sha256(param.getLoginPwd()));
		BackgroundAccount newUserAccount = param.convertToPo();
		backgroundAccountRepo.save(newUserAccount);
	}

	@Transactional
	public void delBackgroundAccount(@NotBlank String accountId) {
		BackgroundAccount account = backgroundAccountRepo.getOne(accountId);
		if (account.getSuperAdminFlag()) {
			throw new BizException("无权操作超级管理员");
		}
		account.deleted();
		backgroundAccountRepo.save(account);
	}

	@Cached(name = "findMenuTreeByAccountId_", key = "args[0]", expire = 3600)
	@Transactional(readOnly = true)
	public List<MenuVO> findMenuTreeByAccountId(@NotBlank String accountId) {
		List<String> roleIds = new ArrayList<>();
		List<AccountRole> accountRoles = accountRoleRepo.findByAccountIdAndRoleDeletedFlagFalse(accountId);
		if (CollectionUtil.isEmpty(accountRoles)) {
			return findMenuTree();
		}
		for (AccountRole accountRole : accountRoles) {
			roleIds.add(accountRole.getRoleId());
		}
		List<String> menuIds = new ArrayList<>();
		List<RoleMenu> roleMenus = roleMenuRepo.findByRoleIdIn(roleIds);
		for (RoleMenu roleMenu : roleMenus) {
			menuIds.add(roleMenu.getMenuId());
		}
		List<Menu> menus = menuRepo.findByIdInAndDeletedFlagFalseOrderByOrderNo(menuIds);
		return buildMenuTree(MenuVO.convertFor(menus));
	}

	@Transactional
	public void assignRole(@Valid AssignRoleParam param) {
		List<AccountRole> assignRoles = accountRoleRepo.findByAccountId(param.getAccountId());
		accountRoleRepo.deleteAll(assignRoles);
		for (String roleId : param.getRoleIds()) {
			accountRoleRepo.save(AccountRole.build(param.getAccountId(), roleId));
		}
	}

	@Transactional(readOnly = true)
	public List<RoleVO> findRoleByAccountId(@NotBlank String accountId) {
		List<RoleVO> roleVOs = new ArrayList<>();
		List<AccountRole> accountRoles = accountRoleRepo.findByAccountId(accountId);
		for (AccountRole accountRole : accountRoles) {
			roleVOs.add(RoleVO.convertFor(accountRole.getRole()));
		}
		return roleVOs;
	}

	@Transactional
	public void assignMenu(@Valid AssignMenuParam param) {
		List<RoleMenu> roleMenus = roleMenuRepo.findByRoleId(param.getRoleId());
		roleMenuRepo.deleteAll(roleMenus);
		for (String menuId : param.getMenuIds()) {
			roleMenuRepo.save(RoleMenu.build(param.getRoleId(), menuId));
		}
	}

	@Transactional(readOnly = true)
	public List<MenuVO> findMenuByRoleId(@NotBlank String roleId) {
		List<MenuVO> menuVOs = new ArrayList<>();
		List<RoleMenu> roleMenus = roleMenuRepo.findByRoleId(roleId);
		for (RoleMenu roleMenu : roleMenus) {
			menuVOs.add(MenuVO.convertFor(roleMenu.getMenu()));
		}
		return menuVOs;
	}

	@Transactional(readOnly = true)
	public List<RoleVO> findAllRole() {
		return RoleVO.convertFor(roleRepo.findByDeletedFlagFalse());
	}

	@Transactional(readOnly = true)
	public RoleVO findRoleById(@NotBlank String id) {
		return RoleVO.convertFor(roleRepo.getOne(id));
	}

	@Transactional
	public void delRole(@NotBlank String id) {
		Role role = roleRepo.getOne(id);
		role.deleted();
		roleRepo.save(role);
	}

	@Transactional
	public void addOrUpdateRole(@Valid RoleParam param) {
		if (StrUtil.isBlank(param.getId())) {
			Role role = param.convertToPo();
			roleRepo.save(role);
		} else {
			Role role = roleRepo.getOne(param.getId());
			BeanUtils.copyProperties(param, role);
			roleRepo.save(role);
		}
	}

	@Transactional(readOnly = true)
	public MenuVO findMenuById(@NotBlank String id) {
		return MenuVO.convertFor(menuRepo.getOne(id));
	}

	@Transactional
	public void delMenu(@NotBlank String id) {
		List<Menu> subMenus = menuRepo.findByParentIdAndDeletedFlagFalse(id);
		for (Menu subMenu : subMenus) {
			List<Menu> btns = menuRepo.findByParentIdAndDeletedFlagFalse(subMenu.getId());
			for (Menu btn : btns) {
				btn.deleted();
				menuRepo.save(btn);
			}
		}
		if (CollectionUtil.isNotEmpty(subMenus)) {
			for (Menu subMenu : subMenus) {
				subMenu.deleted();
				menuRepo.save(subMenu);
			}
		}
		Menu menu = menuRepo.getOne(id);
		menu.deleted();
		menuRepo.save(menu);
	}

	@Transactional
	public void addOrUpdateMenu(@Valid MenuParam param) {
		if (StrUtil.isBlank(param.getId())) {
			Menu menu = param.convertToPo();
			menuRepo.save(menu);
		} else {
			Menu menu = menuRepo.getOne(param.getId());
			BeanUtils.copyProperties(param, menu);
			menuRepo.save(menu);
		}
	}

	@Transactional(readOnly = true)
	public List<MenuVO> findMenuTree() {
		List<Menu> menus = menuRepo.findByDeletedFlagFalseOrderByOrderNo();
		List<MenuVO> menuVOs = MenuVO.convertFor(menus);
		return buildMenuTree(menuVOs);
	}

	public List<MenuVO> buildMenuTree(List<MenuVO> menuVOs) {
		List<MenuVO> menu1s = new ArrayList<>();
		List<MenuVO> menu2s = new ArrayList<>();
		for (MenuVO m : menuVOs) {
			if (Constant.菜单类型_一级菜单.equals(m.getType())) {
				menu1s.add(m);
			}
			if (Constant.菜单类型_二级菜单.equals(m.getType())) {
				menu2s.add(m);
			}
		}
		for (MenuVO menu1 : menu1s) {
			for (MenuVO menu2 : menu2s) {
				if (menu1.getId().equals(menu2.getParentId())) {
					menu1.getSubMenus().add(menu2);
				}
			}
		}
		return menu1s;

	}

}
