package com.nft.admin.backgroundaccount.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nft.backgroundaccount.param.AddBackgroundAccountParam;
import com.nft.backgroundaccount.param.AssignMenuParam;
import com.nft.backgroundaccount.param.AssignRoleParam;
import com.nft.backgroundaccount.param.BackgroundAccountEditParam;
import com.nft.backgroundaccount.param.BackgroundAccountQueryCondParam;
import com.nft.backgroundaccount.param.MenuParam;
import com.nft.backgroundaccount.param.RoleParam;
import com.nft.backgroundaccount.service.RbacService;
import com.nft.backgroundaccount.vo.BackgroundAccountVO;
import com.nft.backgroundaccount.vo.MenuVO;
import com.nft.backgroundaccount.vo.RoleVO;
import com.nft.backgroundaccount.vo.SuperAdminVO;
import com.nft.common.googleauth.GoogleAuthInfoVO;
import com.nft.common.googleauth.GoogleAuthenticator;
import com.nft.common.operlog.OperLog;
import com.nft.common.vo.PageResult;
import com.nft.common.vo.Result;
import com.nft.constants.Constant;

import cn.dev33.satoken.stp.StpUtil;

@Controller
@RequestMapping("/rbac")
public class RbacController {

	public static final String 模块_超级管理员 = "超级管理员";

	public static final String 模块_后台账号 = "后台账号";

	public static final String 模块_后台角色管理 = "后台角色管理";

	public static final String 模块_后台菜单管理 = "后台菜单管理";

	@Autowired
	private RbacService rbacService;

	@OperLog(subSystem = Constant.子系统_后台管理, module = 模块_超级管理员, operate = "修改密码")
	@PostMapping("/modifySuperAdminLoginPwd")
	@ResponseBody
	public Result<String> modifySuperAdminLoginPwd(String id, String newPwd) {
		rbacService.modifySuperAdminLoginPwd(id, newPwd, StpUtil.getLoginIdAsString());
		return Result.success();
	}

	@GetMapping("/findSuperAdmin")
	@ResponseBody
	public Result<List<SuperAdminVO>> findSuperAdmin() {
		return Result.success(rbacService.findSuperAdmin());
	}

	@PostMapping("/bindGoogleAuth")
	@ResponseBody
	public Result<String> bindGoogleAuth(String googleSecretKey, String googleVerCode) {
		rbacService.bindGoogleAuth(StpUtil.getLoginIdAsString(), googleSecretKey, googleVerCode);
		return Result.success();
	}

	@GetMapping("/unBindGoogleAuth")
	@ResponseBody
	public Result<?> unBindGoogleAuth(String id) {
		rbacService.unBindGoogleAuth(id);
		return Result.success();
	}

	@GetMapping("/generateGoogleSecretKey")
	@ResponseBody
	public Result<String> generateGoogleSecretKey() {
		return Result.success().setData(GoogleAuthenticator.generateSecretKey());
	}

	@GetMapping("/getGoogleAuthInfo")
	@ResponseBody
	public Result<GoogleAuthInfoVO> getGoogleAuthInfo(String accountId) {
		return Result.success(rbacService.getGoogleAuthInfo(accountId));
	}

	@GetMapping("/findBackgroundAccountByPage")
	@ResponseBody
	public Result<PageResult<BackgroundAccountVO>> findBackgroundAccountByPage(BackgroundAccountQueryCondParam param) {
		return Result.success(rbacService.findBackgroundAccountByPage(param));
	}

	@OperLog(subSystem = Constant.子系统_后台管理, module = 模块_后台账号, operate = "修改登录密码")
	@PostMapping("/modifyLoginPwd")
	@ResponseBody
	public Result<String> modifyLoginPwd(String id, String newPwd) {
		rbacService.modifyLoginPwd(id, newPwd);
		return Result.success();
	}

	@OperLog(subSystem = Constant.子系统_后台管理, module = 模块_后台账号, operate = "修改账号信息")
	@PostMapping("/updateBackgroundAccount")
	@ResponseBody
	public Result<String> updateBackgroundAccount(BackgroundAccountEditParam param) {
		rbacService.updateBackgroundAccount(param);
		return Result.success();
	}

	@GetMapping("/findBackgroundAccountById")
	@ResponseBody
	public Result<BackgroundAccountVO> findBackgroundAccountById(String id) {
		BackgroundAccountVO accountInfo = rbacService.findBackgroundAccountById(id);
		return Result.success(accountInfo);
	}

	@GetMapping("/getAccountInfo")
	@ResponseBody
	public Result<BackgroundAccountVO> getAccountInfo() {
		BackgroundAccountVO accountInfo = rbacService.findBackgroundAccountById(StpUtil.getLoginIdAsString());
		return Result.success(accountInfo);
	}

	@OperLog(subSystem = Constant.子系统_后台管理, module = 模块_后台账号, operate = "删除账号")
	@GetMapping("/delBackgroundAccount")
	@ResponseBody
	public Result<String> delBackgroundAccount(String id) {
		rbacService.delBackgroundAccount(id);
		return Result.success();
	}

	@OperLog(subSystem = Constant.子系统_后台管理, module = 模块_后台账号, operate = "新增账号")
	@PostMapping("/addBackgroundAccount")
	@ResponseBody
	public Result<String> addBackgroundAccount(AddBackgroundAccountParam param) {
		rbacService.addUserAccount(param);
		return Result.success();
	}

	@GetMapping("/findMyMenuTree")
	@ResponseBody
	public Result<List<MenuVO>> findMyMenuTree() {
		return Result.success(rbacService.findMenuTreeByAccountId(StpUtil.getLoginIdAsString()));
	}

	@OperLog(subSystem = Constant.子系统_后台管理, module = 模块_后台账号, operate = "分配角色")
	@PostMapping("/assignRole")
	@ResponseBody
	public Result<String> assignRole(@RequestBody AssignRoleParam param) {
		rbacService.assignRole(param);
		return Result.success();
	}

	@GetMapping("/findRoleByAccountId")
	@ResponseBody
	public Result<List<RoleVO>> findRoleByAccountId(String accountId) {
		return Result.success(rbacService.findRoleByAccountId(accountId));
	}

	@OperLog(subSystem = Constant.子系统_后台管理, module = 模块_后台角色管理, operate = "分配菜单")
	@PostMapping("/assignMenu")
	@ResponseBody
	public Result<String> assignMenu(@RequestBody AssignMenuParam param) {
		rbacService.assignMenu(param);
		return Result.success();
	}

	@GetMapping("/findMenuByRoleId")
	@ResponseBody
	public Result<List<MenuVO>> findMenuByRoleId(String roleId) {
		return Result.success(rbacService.findMenuByRoleId(roleId));
	}

	@GetMapping("/findAllRole")
	@ResponseBody
	public Result<List<RoleVO>> findAllRole() {
		return Result.success(rbacService.findAllRole());
	}

	@OperLog(subSystem = Constant.子系统_后台管理, module = 模块_后台角色管理, operate = "添加或修改角色")
	@PostMapping("/addOrUpdateRole")
	@ResponseBody
	public Result<String> addOrUpdateRole(RoleParam param) {
		rbacService.addOrUpdateRole(param);
		return Result.success();
	}

	@OperLog(subSystem = Constant.子系统_后台管理, module = 模块_后台角色管理, operate = "删除角色")
	@GetMapping("/delRole")
	@ResponseBody
	public Result<String> delRole(String id) {
		rbacService.delRole(id);
		return Result.success();
	}

	@GetMapping("/findRoleById")
	@ResponseBody
	public Result<RoleVO> findRoleById(String id) {
		return Result.success(rbacService.findRoleById(id));
	}

	@GetMapping("/findMenuById")
	@ResponseBody
	public Result<MenuVO> findMenuById(String id) {
		return Result.success(rbacService.findMenuById(id));
	}

	@OperLog(subSystem = Constant.子系统_后台管理, module = 模块_后台菜单管理, operate = "删除菜单")
	@GetMapping("/delMenu")
	@ResponseBody
	public Result<String> delMenu(String id) {
		rbacService.delMenu(id);
		return Result.success();
	}

	@OperLog(subSystem = Constant.子系统_后台管理, module = 模块_后台菜单管理, operate = "添加或修改菜单")
	@PostMapping("/addOrUpdateMenu")
	@ResponseBody
	public Result<String> addOrUpdateMenu(MenuParam param) {
		rbacService.addOrUpdateMenu(param);
		return Result.success();
	}

	@GetMapping("/findMenuTree")
	@ResponseBody
	public Result<List<MenuVO>> findMenuTree() {
		return Result.success(rbacService.findMenuTree());
	}

}
