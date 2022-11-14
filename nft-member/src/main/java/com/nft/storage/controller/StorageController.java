package com.nft.storage.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
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
import com.nft.member.vo.InviteInfoVO;
import com.nft.setting.service.SettingService;
import com.nft.storage.service.StorageService;
import com.nft.storage.vo.StorageVO;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;

@Controller
@RequestMapping("/storage")
public class StorageController {

	@Autowired
	private StorageService storageService;

	@Autowired
	private SettingService settingService;

	@GetMapping("/inviteQrcode/{code:.+}")
	public ResponseEntity<Resource> inviteQrcode(@PathVariable String code) {
		String h5Gateway = settingService.getH5Gateway();
		InviteInfoVO inviteInfo = InviteInfoVO.build(code, h5Gateway);
		MediaType mediaType = MediaType.parseMediaType("image/png");
		byte[] png = QrCodeUtil.generatePng(inviteInfo.getInviteLink(), 128, 128);
		ByteArrayResource resource = new ByteArrayResource(png);
		return ResponseEntity.ok().contentType(mediaType).body(resource);
	}

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
