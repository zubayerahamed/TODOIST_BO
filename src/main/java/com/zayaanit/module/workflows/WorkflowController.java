package com.zayaanit.module.workflows;

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
 * @since Aug 10, 2025
 */
@RestApiController
@RequestMapping("/api/v1/workflows")
public class WorkflowController {

	@Autowired private WorkflowService workflowService;

	@GetMapping("/all/workspace")
	public ResponseEntity<SuccessResponse<List<WorkflowResDto>>> getAllWorkspaceWorkflows(){
		List<WorkflowResDto> resData = workflowService.getAllWorkspaceWorkflows();
		return ResponseBuilder.build(ResponseStatusType.READ_SUCCESS, resData);
	}

	@GetMapping("/all/project/{id}")
	public ResponseEntity<SuccessResponse<List<WorkflowResDto>>> getAllProjectCategories(@PathVariable Long id){
		List<WorkflowResDto> resData = workflowService.getAllProjectWorkflows(id);
		return ResponseBuilder.build(ResponseStatusType.READ_SUCCESS, resData);
	}

	@GetMapping("/{id}")
	public ResponseEntity<SuccessResponse<WorkflowResDto>> find(@PathVariable Long id){
		WorkflowResDto resData = workflowService.findById(id);
		return ResponseBuilder.build(ResponseStatusType.READ_SUCCESS, resData);
	} 

	@PostMapping
	public ResponseEntity<SuccessResponse<WorkflowResDto>> create(@Valid @RequestBody CreateWorkflowReqDto reqDto){
		WorkflowResDto resData = workflowService.create(reqDto);
		return ResponseBuilder.build(ResponseStatusType.CREATE_SUCCESS, resData);
	}

	@PutMapping
	public ResponseEntity<SuccessResponse<WorkflowResDto>> update(@RequestBody UpdateWorkflowReqDto reqDto){
		WorkflowResDto resData = workflowService.update(reqDto);
		return ResponseBuilder.build(ResponseStatusType.UPDATE_SUCCESS, resData);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<SuccessResponse<WorkflowResDto>> delete(@PathVariable Long id){
		workflowService.deleteById(id);
		return ResponseBuilder.build(ResponseStatusType.DELETE_SUCCESS, null);
	}
}
