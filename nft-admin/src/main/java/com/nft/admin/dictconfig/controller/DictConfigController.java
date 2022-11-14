package com.nft.admin.dictconfig.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nft.common.operlog.OperLog;
import com.nft.common.vo.PageResult;
import com.nft.common.vo.Result;
import com.nft.constants.Constant;
import com.nft.dictconfig.DictHolder;
import com.nft.dictconfig.param.AddOrUpdateDictTypeParam;
import com.nft.dictconfig.param.DictTypeQueryCondParam;
import com.nft.dictconfig.param.UpdateDictDataParam;
import com.nft.dictconfig.service.DictService;
import com.nft.dictconfig.vo.DictItemVO;
import com.nft.dictconfig.vo.DictTypeVO;

@Controller
@RequestMapping("/dictconfig")
public class DictConfigController {

	public static final String 模块_字典管理 = "字典管理";

	@Autowired
	private DictService dictService;

	@OperLog(subSystem = Constant.子系统_后台管理, module = 模块_字典管理, operate = "更新字典数据")
	@PostMapping("/updateDictData")
	@ResponseBody
	public Result<String> updateDictData(@RequestBody UpdateDictDataParam param) {
		dictService.updateDictData(param);
		return Result.success();
	}

	@GetMapping("/findDictItemByDictTypeId")
	@ResponseBody
	public Result<List<DictItemVO>> findDictItemByDictTypeId(String dictTypeId) {
		return Result.success(dictService.findDictItemByDictTypeId(dictTypeId));
	}

	@OperLog(subSystem = Constant.子系统_后台管理, module = 模块_字典管理, operate = "删除字典")
	@GetMapping("/delDictType")
	@ResponseBody
	public Result<String> delDictType(String id) {
		dictService.delDictType(id);
		return Result.success();
	}

	@OperLog(subSystem = Constant.子系统_后台管理, module = 模块_字典管理, operate = "添加或修改字典")
	@PostMapping("/addOrUpdateDictType")
	@ResponseBody
	public Result<String> addOrUpdateDictType(AddOrUpdateDictTypeParam param) {
		dictService.addOrUpdateDictType(param);
		return Result.success();
	}

	@GetMapping("/findDictTypeById")
	@ResponseBody
	public Result<DictTypeVO> findDictTypeById(String id) {
		return Result.success(dictService.findDictTypeById(id));
	}

	@GetMapping("/findDictTypeByPage")
	@ResponseBody
	public Result<PageResult<DictTypeVO>> findDictTypeByPage(DictTypeQueryCondParam param) {
		return Result.success(dictService.findDictTypeByPage(param));
	}

	@GetMapping("/findDictItemInCache")
	@ResponseBody
	public Result<List<DictItemVO>> findDictItemInCache(String dictTypeCode) {
		return Result.success(DictHolder.findDictItem(dictTypeCode));
	}

}
