package com.nft.collection.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nft.collection.param.CollectionQueryCondParam;
import com.nft.collection.param.ResaleCollectionQueryCondParam;
import com.nft.collection.service.CollectionService;
import com.nft.collection.service.CreatorService;
import com.nft.collection.vo.CreatorVO;
import com.nft.collection.vo.GroupByDateCollectionVO;
import com.nft.collection.vo.IssuedCollectionActionLogVO;
import com.nft.collection.vo.LatestCollectionDetailVO;
import com.nft.collection.vo.LatestCollectionVO;
import com.nft.collection.vo.PublishedBrandDictVO;
import com.nft.collection.vo.ResaleCollectionDetailVO;
import com.nft.collection.vo.ResaleCollectionVO;
import com.nft.common.vo.PageResult;
import com.nft.common.vo.Result;

@RestController
@RequestMapping("/collection")
public class CollectionController {

	@Autowired
	private CollectionService collectionService;
	
	@Autowired
	private CreatorService creatorService;
	
	@GetMapping("/findCreatorById")
	public Result<CreatorVO> findCreatorById(String id) {
		return Result.success(creatorService.findCreatorById(id));
	}

	@GetMapping("/findPublishedBrandAndCollectionDict")
	public Result<List<PublishedBrandDictVO>> findPublishedBrandAndCollectionDict() {
		return Result.success(collectionService.findPublishedBrandAndCollectionDict());
	}

	@GetMapping("/findIssuedCollectionActionLog")
	public Result<List<IssuedCollectionActionLogVO>> findIssuedCollectionActionLog(String issuedCollectionId) {
		return Result.success(collectionService.findIssuedCollectionActionLog(issuedCollectionId));
	}

	@GetMapping("/findResaleCollectionDetail")
	public Result<ResaleCollectionDetailVO> findResaleCollectionDetail(String id) {
		return Result.success(collectionService.findResaleCollectionDetail(id));
	}

	@GetMapping("/findResaleCollectionByPage")
	public Result<PageResult<ResaleCollectionVO>> findResaleCollectionByPage(ResaleCollectionQueryCondParam param) {
		return Result.success(collectionService.findResaleCollectionByPage(param));
	}

	@GetMapping("/findLatestCollectionDetailById")
	public Result<LatestCollectionDetailVO> findLatestCollectionDetailById(String id) {
		return Result.success(collectionService.findLatestCollectionDetailById(id));
	}

	@GetMapping("/findLatestMysteryBoxByPage")
	public Result<PageResult<LatestCollectionVO>> findLatestMysteryBoxByPage(CollectionQueryCondParam param) {
		return Result.success(collectionService.findLatestMysteryBoxByPage(param));
	}

	@GetMapping("/findLatestCollectionByPage")
	public Result<PageResult<LatestCollectionVO>> findLatestCollectionByPage(CollectionQueryCondParam param) {
		return Result.success(collectionService.findLatestCollectionByPage(param));
	}

	@GetMapping("/findForSaleCollection")
	public Result<List<GroupByDateCollectionVO>> findForSaleCollection() {
		return Result.success(collectionService.findForSaleCollection());
	}

}
