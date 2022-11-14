package com.nft.admin.storage.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.nft.common.exception.BizException;
import com.nft.common.vo.Result;
import com.nft.storage.service.StorageService;
import com.nft.storage.vo.StorageVO;

import cn.hutool.core.util.ArrayUtil;

@Controller
@RequestMapping("/storage")
public class StorageController {

	@Autowired
	private StorageService storageService;

	@GetMapping("/fetch/{id:.+}")
	public ResponseEntity<Resource> fetch(@PathVariable String id) {
		StorageVO vo = storageService.findById(id);
		if (vo == null) {
			return ResponseEntity.notFound().build();
		}

		String fileType = vo.getFileType();
		MediaType mediaType = MediaType.parseMediaType(fileType);
		Resource file = storageService.loadAsResource(vo.getId());
		if (file == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().contentType(mediaType).body(file);
	}

	@PostMapping("/upload")
	@ResponseBody
	public Result<List<String>> upload(@RequestParam("file_data") MultipartFile[] files) throws IOException {
		if (ArrayUtil.isEmpty(files)) {
			throw new BizException("请选择要上传的图片");
		}
		List<String> storageIds = new ArrayList<>();
		for (MultipartFile file : files) {
			String filename = file.getOriginalFilename();
			String storageId = storageService.upload(file.getInputStream(), file.getSize(), file.getContentType(),
					filename);
			storageIds.add(storageId);
		}
		return Result.success(storageIds);
	}

}
