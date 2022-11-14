package com.nft.dictconfig.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nft.common.vo.Result;
import com.nft.dictconfig.DictHolder;
import com.nft.dictconfig.vo.DictItemVO;

@Controller
@RequestMapping("/dictconfig")
public class DictConfigController {
	
	@GetMapping("/findDictItemInCache")
	@ResponseBody
	public Result<List<DictItemVO>> findDictItemInCache(String dictTypeCode) {
		return Result.success(DictHolder.findDictItem(dictTypeCode));
	}

}
