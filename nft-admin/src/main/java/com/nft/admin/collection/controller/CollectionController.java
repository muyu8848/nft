package com.nft.admin.collection.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nft.chain.service.ChainService;
import com.nft.collection.param.AddCollectionParam;
import com.nft.collection.param.CollectionQueryCondParam;
import com.nft.collection.param.UpdateCollectionStoryParam;
import com.nft.collection.param.UpdateMysteryBoxCommodityParam;
import com.nft.collection.service.CollectionService;
import com.nft.collection.vo.CollectionStatisticDataVO;
import com.nft.collection.vo.CollectionVO;
import com.nft.collection.vo.IssuedCollectionVO;
import com.nft.collection.vo.MysteryBoxCommodityVO;
import com.nft.common.operlog.OperLog;
import com.nft.common.vo.PageResult;
import com.nft.common.vo.Result;
import com.nft.constants.Constant;

@Controller
@RequestMapping("/collection")
public class CollectionController {

	public static final String 模块_艺术品管理 = "艺术品管理";

	@Autowired
	private CollectionService collectionService;

	@Autowired
	private ChainService chainService;

	@GetMapping("/findMysteryBoxCommodity")
	@ResponseBody
	public Result<List<MysteryBoxCommodityVO>> findMysteryBoxCommodity(String collectionId) {
		return Result.success(collectionService.findMysteryBoxCommodity(collectionId));
	}

	@OperLog(subSystem = Constant.子系统_后台管理, module = 模块_艺术品管理, operate = "编辑盲盒商品")
	@PostMapping("/updateMysteryBoxCommodity")
	@ResponseBody
	public Result<String> updateMysteryBoxCommodity(@RequestBody UpdateMysteryBoxCommodityParam param) {
		collectionService.updateMysteryBoxCommodity(param);
		return Result.success();
	}

	@GetMapping("/findIssuedCollection")
	@ResponseBody
	public Result<List<IssuedCollectionVO>> findIssuedCollection(String collectionId) {
		return Result.success(collectionService.findIssuedCollection(collectionId));
	}

	@OperLog(subSystem = Constant.子系统_后台管理, module = 模块_艺术品管理, operate = "手动上链")
	@GetMapping("/syncChain")
	@ResponseBody
	public Result<String> syncChain(String id) {
		return Result.success(chainService.chainAddArtwork(id));
	}

	@GetMapping("/getCollectionStatisticData")
	@ResponseBody
	public Result<CollectionStatisticDataVO> getCollectionStatisticData() {
		return Result.success(collectionService.getCollectionStatisticData());
	}

	@OperLog(subSystem = Constant.子系统_后台管理, module = 模块_艺术品管理, operate = "编辑作品故事")
	@PostMapping("/updateCollectionStory")
	@ResponseBody
	public Result<String> updateCollectionStory(@RequestBody UpdateCollectionStoryParam param) {
		collectionService.updateCollectionStory(param);
		return Result.success();
	}

	@OperLog(subSystem = Constant.子系统_后台管理, module = 模块_艺术品管理, operate = "删除")
	@GetMapping("/delCollection")
	@ResponseBody
	public Result<String> delCollection(String id) {
		collectionService.delCollection(id);
		return Result.success();
	}

	@GetMapping("/findAllCollection")
	@ResponseBody
	public Result<List<CollectionVO>> findAllCollection() {
		return Result.success(collectionService.findAllCollection());
	}

	@GetMapping("/findCollectionById")
	@ResponseBody
	public Result<CollectionVO> findCollectionById(String id) {
		return Result.success(collectionService.findCollectionById(id));
	}

	@GetMapping("/findCollectionByPage")
	@ResponseBody
	public Result<PageResult<CollectionVO>> findCollectionByPage(CollectionQueryCondParam param) {
		return Result.success(collectionService.findCollectionByPage(param));
	}

	@OperLog(subSystem = Constant.子系统_后台管理, module = 模块_艺术品管理, operate = "新增")
	@PostMapping("/addCollection")
	@ResponseBody
	public Result<String> addCollection(AddCollectionParam param) {
		collectionService.addCollection(param);
		return Result.success();
	}

}
