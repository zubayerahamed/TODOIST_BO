package com.zayaanit.module.sections;

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
@RequestMapping("/api/v1/sections")
public class SectionController {

    @Autowired private SectionService sectionService;

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<SectionResDto>> findByTagId(Long id, Long projectId) {
        SectionResDto resData = sectionService.findSectionById(id,projectId);
        return ResponseBuilder.build(ResponseStatusType.READ_SUCCESS, resData);
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<SectionResDto>> createSection(@Valid @RequestBody CreateSectionReqDto reqDto) {
        SectionResDto resData = sectionService.createSection(reqDto);
        return ResponseBuilder.build(ResponseStatusType.CREATE_SUCCESS, resData);
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<List<SectionResDto>>> getAllSection(Long projectId) {
        List<SectionResDto> resData = sectionService.getAllSections(projectId);
        return ResponseBuilder.build(ResponseStatusType.READ_SUCCESS, resData);
    }
}
