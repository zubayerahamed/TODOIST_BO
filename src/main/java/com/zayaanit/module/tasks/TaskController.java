package com.zayaanit.module.tasks;

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
 * 
 * @since Jul 3, 2025
 */
@RestApiController
@RequestMapping("/api/v1/tasks")
public class TaskController {

	@Autowired
	private TaskService taskService;

	@GetMapping("/all/{projectId}")
	public ResponseEntity<SuccessResponse<List<TaskResDto>>> getAll(@PathVariable Long projectId){
		List<TaskResDto> resData = taskService.getAllByProjectId(projectId);
		return ResponseBuilder.build(ResponseStatusType.READ_SUCCESS, resData);
	}

	@GetMapping("/find/{id}")
	public ResponseEntity<SuccessResponse<TaskResDto>> find(@PathVariable Long id){
		TaskResDto resData = taskService.findById(id);
		return ResponseBuilder.build(ResponseStatusType.READ_SUCCESS, resData);
	} 

	@PostMapping
	public ResponseEntity<SuccessResponse<TaskResDto>> create(@Valid @RequestBody CreateTaskReqDto reqDto){
		TaskResDto resData = taskService.create(reqDto);
		return ResponseBuilder.build(ResponseStatusType.CREATE_SUCCESS, resData);
	}

	@PutMapping
	public ResponseEntity<SuccessResponse<TaskResDto>> update(@Valid @RequestBody UpdateTaskReqDto reqDto){
		TaskResDto resData = taskService.update(reqDto);
		return ResponseBuilder.build(ResponseStatusType.CREATE_SUCCESS, resData);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<SuccessResponse<TaskResDto>> delete(@PathVariable Long id){
		taskService.delete(id);
		return ResponseBuilder.build(ResponseStatusType.DELETE_NO_CONTENT, null);
	} 
}
