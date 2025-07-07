package com.zayaanit.module.tags;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zayaanit.enums.ResponseStatusType;
import com.zayaanit.model.ResponseBuilder;
import com.zayaanit.model.SuccessResponse;
import com.zayaanit.module.RestApiController;

import jakarta.validation.Valid;

@RestApiController
@RequestMapping("/api/v1/projects")
public class TagController {

    @Autowired
    private TagService tagService;

    @PostMapping
    public ResponseEntity<SuccessResponse<CreateTagResDto>> createProject(@Valid @RequestBody CreateTagReqDto reqDto) {
        CreateTagResDto resData = tagService.createTag(reqDto);
        return ResponseBuilder.build(ResponseStatusType.CREATE_SUCCESS, resData);
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<List<CreateTagResDto>>> getAllProject() {
        List<CreateTagResDto> resData = tagService.getAllTags();
        return ResponseBuilder.build(ResponseStatusType.READ_SUCCESS, resData);
    }

}
