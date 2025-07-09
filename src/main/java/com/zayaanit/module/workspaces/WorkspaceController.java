package com.zayaanit.module.workspaces;

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
 * @since Jul 5, 2025
 */
@RestApiController
@RequestMapping("/api/v1/workspaces")
public class WorkspaceController {

	@Autowired private WorkspaceService workspaceService;

	@GetMapping
	public ResponseEntity<SuccessResponse<List<WorkspaceResDto>>> getAll(){
		List<WorkspaceResDto> resData = workspaceService.getAll();
		return ResponseBuilder.build(ResponseStatusType.READ_SUCCESS, resData);
	}

	@GetMapping("/{id}")
	public ResponseEntity<SuccessResponse<WorkspaceResDto>> find(@PathVariable Long id){
		WorkspaceResDto resData = workspaceService.findById(id);
		return ResponseBuilder.build(ResponseStatusType.READ_SUCCESS, resData);
	} 

	@PostMapping
	public ResponseEntity<SuccessResponse<CreateWorkspaceResDto>> create(@Valid @RequestBody CreateWorkspaceReqDto reqDto){
		CreateWorkspaceResDto resData = workspaceService.create(reqDto);
		return ResponseBuilder.build(ResponseStatusType.CREATE_SUCCESS, resData);
	}

	@PutMapping
	public ResponseEntity<SuccessResponse<UpdateWorkspaceResDto>> update(@Valid @RequestBody UpdateWorkspaceReqDto reqDto){
		UpdateWorkspaceResDto resData = workspaceService.update(reqDto);
		return ResponseBuilder.build(ResponseStatusType.UPDATE_SUCCESS, resData);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<SuccessResponse<WorkspaceResDto>> delete(@PathVariable Long id){
		workspaceService.deleteById(id);
		return ResponseBuilder.build(ResponseStatusType.DELETE_SUCCESS, null);
	}
}
