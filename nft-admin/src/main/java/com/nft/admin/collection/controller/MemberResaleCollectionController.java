package com.nft.admin.collection.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nft.collection.param.ResaleCollectionQueryCondParam;
import com.nft.collection.service.CollectionService;
import com.nft.collection.vo.MemberResaleCollectionVO;
import com.nft.common.vo.PageResult;
import com.nft.common.vo.Result;

@Controller
@RequestMapping("/memberResaleCollection")
public class MemberResaleCollectionController {

	@Autowired
	private CollectionService collectionService;

	@GetMapping("/findMemberResaleCollectionByPage")
	@ResponseBody
	public Result<PageResult<MemberResaleCollectionVO>> findMemberResaleCollectionByPage(
			ResaleCollectionQueryCondParam param) {
		return Result.success(collectionService.findMemberResaleCollectionByPage(param));
	}

}
