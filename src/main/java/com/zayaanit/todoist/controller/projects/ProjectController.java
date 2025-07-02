package com.zayaanit.todoist.controller.projects;

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

import com.zayaanit.todoist.anno.RestApiController;
import com.zayaanit.todoist.enums.ResponseStatusType;
import com.zayaanit.todoist.model.ResponseBuilder;
import com.zayaanit.todoist.model.SuccessResponse;

/**
 * Zubayer Ahamed
 * @since Jul 2, 2025
 */
@RestApiController
@RequestMapping("/api/v1/projects")
public class ProjectController {

	@Autowired private ProjectService projectService;

	@GetMapping
	public ResponseEntity<SuccessResponse<List<ProjectResDto>>> getAllProject(){
		List<ProjectResDto> resData = projectService.getAllProject();
		return ResponseBuilder.build(ResponseStatusType.READ_SUCCESS, resData);
	}

	@GetMapping("/{id}")
	public ResponseEntity<SuccessResponse<ProjectResDto>> findProject(@PathVariable("id") Long id){
		ProjectResDto resData = projectService.findById(id);
		return ResponseBuilder.build(ResponseStatusType.READ_SUCCESS, resData);
	} 

	@PostMapping
	public ResponseEntity<SuccessResponse<CreateProjectResDto>> createProject(@RequestBody CreateProjectReqDto reqDto){
		CreateProjectResDto resData = projectService.createProject(reqDto);
		return ResponseBuilder.build(ResponseStatusType.CREATE_SUCCESS, resData);
	}

	@PutMapping
	public ResponseEntity<SuccessResponse<UpdateProjectResDto>> updateProject(@RequestBody UpdateProjectReqDto reqDto){
		UpdateProjectResDto resData = projectService.updateProject(reqDto);
		return ResponseBuilder.build(ResponseStatusType.UPDATE_SUCCESS, resData);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<SuccessResponse<ProjectResDto>> deleteProject(@PathVariable("id") Long id){
		projectService.deleteById(id);
		return ResponseBuilder.build(ResponseStatusType.DELETE_SUCCESS, null);
	}
}
