package com.nft.collection.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nft.collection.param.MemberHoldCollectionQueryCondParam;
import com.nft.collection.param.ResaleCollectionQueryCondParam;
import com.nft.collection.service.CollectionService;
import com.nft.collection.vo.MyHoldCollectionDetailVO;
import com.nft.collection.vo.MyHoldCollectionVO;
import com.nft.collection.vo.MyHoldVO;
import com.nft.collection.vo.MyResaleCollectionDetailVO;
import com.nft.collection.vo.MyResaleCollectionVO;
import com.nft.collection.vo.MySoldCollectionVO;
import com.nft.common.vo.PageResult;
import com.nft.common.vo.Result;

import cn.dev33.satoken.stp.StpUtil;

@RestController
@RequestMapping("/myArtwork")
public class MyArtworkController {

	@Autowired
	private CollectionService collectionService;
	
	@GetMapping("/findMyResaleCollectionDetail")
	public Result<MyResaleCollectionDetailVO> findMyResaleCollectionDetail(String id) {
		return Result.success(collectionService.findMyResaleCollectionDetail(id, StpUtil.getLoginIdAsString()));
	}

	@GetMapping("/findMyHoldCollectionDetail")
	public Result<MyHoldCollectionDetailVO> findMyHoldCollectionDetail(String id) {
		return Result.success(collectionService.findMyHoldCollectionDetail(id, StpUtil.getLoginIdAsString()));
	}

	@GetMapping("/findMySoldCollectionByPage")
	public Result<PageResult<MySoldCollectionVO>> findMySoldCollectionByPage(ResaleCollectionQueryCondParam param) {
		param.setMemberId(StpUtil.getLoginIdAsString());
		return Result.success(collectionService.findMySoldCollectionByPage(param));
	}

	@GetMapping("/findAllMyHold")
	public Result<List<MyHoldVO>> findAllMyHold() {
		return Result.success(collectionService.findAllMyHold(StpUtil.getLoginIdAsString()));
	}
	
	@GetMapping("/findMyHoldMysteryBoxByPage")
	public Result<PageResult<MyHoldCollectionVO>> findMyHoldMysteryBoxByPage(MemberHoldCollectionQueryCondParam param) {
		param.setMemberId(StpUtil.getLoginIdAsString());
		return Result.success(collectionService.findMyHoldMysteryBoxByPage(param));
	}

	@GetMapping("/findMyHoldCollectionByPage")
	public Result<PageResult<MyHoldCollectionVO>> findMyHoldCollectionByPage(MemberHoldCollectionQueryCondParam param) {
		param.setMemberId(StpUtil.getLoginIdAsString());
		return Result.success(collectionService.findMyHoldCollectionByPage(param));
	}
	
	@GetMapping("/findMyResaleCollectionByPage")
	public Result<PageResult<MyResaleCollectionVO>> findMyResaleCollectionByPage(ResaleCollectionQueryCondParam param) {
		param.setMemberId(StpUtil.getLoginIdAsString());
		return Result.success(collectionService.findMyResaleCollectionByPage(param));
	}

}
