package com.zayaanit.module.tags;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zayaanit.enums.ResponseStatusType;
import com.zayaanit.model.ResponseBuilder;
import com.zayaanit.model.SuccessResponse;
import com.zayaanit.module.RestApiController;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PutMapping;

@RestApiController
@RequestMapping("/api/v1/tags")
public class TagController {

	@Autowired
	private TagService tagService;

	@GetMapping
	public ResponseEntity<SuccessResponse<List<TagResDto>>> getAll() {
		List<TagResDto> resData = tagService.getAllTags();
		return ResponseBuilder.build(ResponseStatusType.READ_SUCCESS, resData);
	}

	@GetMapping("/{id}")
	public ResponseEntity<SuccessResponse<TagResDto>> find(Long id) {
		TagResDto resData = tagService.findById(id);
		return ResponseBuilder.build(ResponseStatusType.READ_SUCCESS, resData);
	}

	@PostMapping
	public ResponseEntity<SuccessResponse<TagResDto>> create(@Valid @RequestBody CreateTagReqDto reqDto) {
		TagResDto resData = tagService.createTag(reqDto);
		return ResponseBuilder.build(ResponseStatusType.CREATE_SUCCESS, resData);
	}

	@PutMapping
	public ResponseEntity<SuccessResponse<UpdateTagResDto>> update(@Valid @RequestBody UpdateTagReqDto reqDto) {
		UpdateTagResDto resData = tagService.updateTag(reqDto);
		return ResponseBuilder.build(ResponseStatusType.UPDATE_SUCCESS, resData);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<SuccessResponse<Void>> delete(Long id) {
		tagService.deleteById(id);
		return ResponseBuilder.build(ResponseStatusType.DELETE_SUCCESS, null);
	}
}
