package com.zayaanit.module.projects;

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

/**
 * Zubayer Ahamed
 * @since Jul 2, 2025
 */
@RestApiController
@RequestMapping("/api/v1/projects")
public class ProjectController {

	@Autowired private ProjectService projectService;

	@GetMapping
	public ResponseEntity<SuccessResponse<List<ProjectResDto>>> getAll(){
		List<ProjectResDto> resData = projectService.getAll();
		return ResponseBuilder.build(ResponseStatusType.READ_SUCCESS, resData);
	}

	@GetMapping("/{id}")
	public ResponseEntity<SuccessResponse<ProjectResDto>> find(@PathVariable Long id){
		ProjectResDto resData = projectService.findById(id);
		return ResponseBuilder.build(ResponseStatusType.READ_SUCCESS, resData);
	} 

	@PostMapping
	public ResponseEntity<SuccessResponse<CreateProjectResDto>> create(@Valid @RequestBody CreateProjectReqDto reqDto){
		CreateProjectResDto resData = projectService.create(reqDto);
		return ResponseBuilder.build(ResponseStatusType.CREATE_SUCCESS, resData);
	}

	@PutMapping
	public ResponseEntity<SuccessResponse<UpdateProjectResDto>> update(@Valid @RequestBody UpdateProjectReqDto reqDto){
		UpdateProjectResDto resData = projectService.update(reqDto);
		return ResponseBuilder.build(ResponseStatusType.UPDATE_SUCCESS, resData);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<SuccessResponse<ProjectResDto>> delete(@PathVariable Long id){
		projectService.deleteById(id);
		return ResponseBuilder.build(ResponseStatusType.DELETE_SUCCESS, null);
	}
}
