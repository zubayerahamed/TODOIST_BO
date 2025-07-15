package com.zayaanit.module.sections;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @Autowired
    private SectionService sectionService;

    @GetMapping
    public ResponseEntity<SuccessResponse<List<SectionResDto>>> getAll(Long projectId) {
        List<SectionResDto> resData = sectionService.getAllSections(projectId);
        return ResponseBuilder.build(ResponseStatusType.READ_SUCCESS, resData);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<SectionResDto>> findById(@PathVariable Long id) {
        SectionResDto resData = sectionService.findSectionById(id);
        return ResponseBuilder.build(ResponseStatusType.READ_SUCCESS, resData);
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<SectionResDto>> create(@Valid @RequestBody CreateSectionReqDto reqDto) {
        SectionResDto resData = sectionService.createSection(reqDto);
        return ResponseBuilder.build(ResponseStatusType.CREATE_SUCCESS, resData);
    }


    @PutMapping
    public ResponseEntity<SuccessResponse<SectionResDto>> update(@Valid @RequestBody UpdateSectionReqDto reqDto) {
        SectionResDto resData = sectionService.updateSection(reqDto);
        return ResponseBuilder.build(ResponseStatusType.UPDATE_SUCCESS, resData);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<Void>> delete(@PathVariable Long id) {
        sectionService.deleteSection(id);
        return ResponseBuilder.build(ResponseStatusType.DELETE_SUCCESS, null);
    }
}
