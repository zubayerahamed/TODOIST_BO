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

    @PostMapping
    public ResponseEntity<SuccessResponse<TagResDto>> createProject(@Valid @RequestBody CreateTagReqDto reqDto) {
        TagResDto resData = tagService.createTag(reqDto);
        return ResponseBuilder.build(ResponseStatusType.CREATE_SUCCESS, resData);
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<List<TagResDto>>> getAllProject() {
        List<TagResDto> resData = tagService.getAllTags();
        return ResponseBuilder.build(ResponseStatusType.READ_SUCCESS, resData);
    }

    @PutMapping
    public ResponseEntity<SuccessResponse<UpdateTagResDto>> updateTag(@Valid @RequestBody UpdateTagReqDto reqDto) {
        UpdateTagResDto resData = tagService.updateTag(reqDto);
        return ResponseBuilder.build(ResponseStatusType.UPDATE_SUCCESS, resData);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<TagResDto>> findByTagId(Long id) {
        TagResDto resData = tagService.findById(id);
        return ResponseBuilder.build(ResponseStatusType.READ_SUCCESS, resData);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<Void>> deleteTag(Long id) {
        tagService.deleteById(id);
        return ResponseBuilder.build(ResponseStatusType.DELETE_SUCCESS, null);
    }
}
