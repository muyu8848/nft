package com.nft.admin.collection.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nft.collection.param.CollectionGiveRecordQueryCondParam;
import com.nft.collection.service.CollectionGiveService;
import com.nft.collection.vo.CollectionGiveRecordVO;
import com.nft.common.vo.PageResult;
import com.nft.common.vo.Result;

@Controller
@RequestMapping("/collectionGiveRecord")
public class CollectionGiveRecordController {

	@Autowired
	private CollectionGiveService collectionGiveService;

	@GetMapping("/findCollectionGiveRecordByPage")
	@ResponseBody
	public Result<PageResult<CollectionGiveRecordVO>> findCollectionGiveRecordByPage(
			CollectionGiveRecordQueryCondParam param) {
		return Result.success(collectionGiveService.findCollectionGiveRecordByPage(param));
	}

}
