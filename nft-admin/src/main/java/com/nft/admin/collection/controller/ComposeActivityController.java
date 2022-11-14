package com.nft.admin.collection.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nft.collection.param.AddComposeActivityParam;
import com.nft.collection.param.ComposeActivityQueryCondParam;
import com.nft.collection.param.UpdateComposeMaterialParam;
import com.nft.collection.service.ComposeActivityService;
import com.nft.collection.vo.ComposeActivityVO;
import com.nft.collection.vo.ComposeRecordVO;
import com.nft.common.operlog.OperLog;
import com.nft.common.vo.PageResult;
import com.nft.common.vo.Result;
import com.nft.constants.Constant;

@Controller
@RequestMapping("/composeActivity")
public class ComposeActivityController {

	public static final String 模块_合成活动 = "合成活动";

	@Autowired
	private ComposeActivityService composeActivityService;

	@GetMapping("/findComposeRecord")
	@ResponseBody
	public Result<List<ComposeRecordVO>> findComposeRecord(String composeActivityId, String memberMobile) {
		return Result.success(composeActivityService.findComposeRecord(composeActivityId, memberMobile));
	}

	@OperLog(subSystem = Constant.子系统_后台管理, module = 模块_合成活动, operate = "删除活动")
	@GetMapping("/del")
	@ResponseBody
	public Result<String> del(String id) {
		composeActivityService.del(id);
		return Result.success();
	}

	@GetMapping("/findComposeActivityByPage")
	@ResponseBody
	public Result<PageResult<ComposeActivityVO>> findComposeActivityByPage(ComposeActivityQueryCondParam param) {
		return Result.success(composeActivityService.findComposeActivityByPage(param));
	}

	@GetMapping("/findComposeActivityById")
	@ResponseBody
	public Result<ComposeActivityVO> findComposeActivityById(String id) {
		return Result.success(composeActivityService.findComposeActivityById(id));
	}

	@OperLog(subSystem = Constant.子系统_后台管理, module = 模块_合成活动, operate = "编辑材料")
	@PostMapping("/updateComposeMaterial")
	@ResponseBody
	public Result<String> updateComposeMaterial(@RequestBody UpdateComposeMaterialParam param) {
		composeActivityService.updateComposeMaterial(param);
		return Result.success();
	}

	@OperLog(subSystem = Constant.子系统_后台管理, module = 模块_合成活动, operate = "新增活动")
	@PostMapping("/addComposeActivity")
	@ResponseBody
	public Result<String> addComposeActivity(AddComposeActivityParam param) {
		composeActivityService.addComposeActivity(param);
		return Result.success();
	}

}
