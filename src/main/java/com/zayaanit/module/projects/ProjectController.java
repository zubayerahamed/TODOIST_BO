package com.zayaanit.module.projects;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

	@PutMapping("/add-to-favourite/{id}")
	public ResponseEntity<SuccessResponse<ProjectResDto>> addToFavourite(@PathVariable Long id){
		ProjectResDto resData = projectService.addToFavourite(id);
		return ResponseBuilder.build(ResponseStatusType.UPDATE_SUCCESS, resData);
	}

	@PutMapping("/remove-from-favourite/{id}")
	public ResponseEntity<SuccessResponse<ProjectResDto>> removeFromFavourite(@PathVariable Long id){
		ProjectResDto resData = projectService.removeFromFavourite(id);
		return ResponseBuilder.build(ResponseStatusType.UPDATE_SUCCESS, resData);
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

	//Paginated getAll
	@GetMapping("/paginated")
	public ResponseEntity<SuccessResponse<Page<ProjectResDto>>> getAll(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size){
		Page<ProjectResDto> resData = projectService.getAll(page, size);
		return ResponseBuilder.build(ResponseStatusType.READ_SUCCESS, resData);
	}
}
