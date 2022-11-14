package com.nft.admin.collection.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nft.collection.param.AddOrUpdateCreatorParam;
import com.nft.collection.param.CreatorQueryCondParam;
import com.nft.collection.service.CreatorService;
import com.nft.collection.vo.CreatorVO;
import com.nft.common.operlog.OperLog;
import com.nft.common.vo.PageResult;
import com.nft.common.vo.Result;
import com.nft.constants.Constant;

@Controller
@RequestMapping("/creator")
public class CreatorController {

	public static final String 模块_创作者列表 = "创作者列表";

	@Autowired
	private CreatorService creatorService;

	@OperLog(subSystem = Constant.子系统_后台管理, module = 模块_创作者列表, operate = "删除创作者")
	@GetMapping("/delCreator")
	@ResponseBody
	public Result<String> delCreator(String id) {
		creatorService.delCreator(id);
		return Result.success();
	}

	@GetMapping("/findCreatorById")
	@ResponseBody
	public Result<CreatorVO> findCreatorById(String id) {
		return Result.success(creatorService.findCreatorById(id));
	}

	@GetMapping("/findAllCreator")
	@ResponseBody
	public Result<List<CreatorVO>> findAllCreator() {
		return Result.success(creatorService.findAllCreator());
	}

	@GetMapping("/findCreatorByPage")
	@ResponseBody
	public Result<PageResult<CreatorVO>> findCreatorByPage(CreatorQueryCondParam param) {
		return Result.success(creatorService.findCreatorByPage(param));
	}

	@OperLog(subSystem = Constant.子系统_后台管理, module = 模块_创作者列表, operate = "新增或编辑创作者")
	@PostMapping("/addOrUpdateCreator")
	@ResponseBody
	public Result<String> addOrUpdateCreator(AddOrUpdateCreatorParam param) {
		creatorService.addOrUpdateCreator(param);
		return Result.success();
	}

}
