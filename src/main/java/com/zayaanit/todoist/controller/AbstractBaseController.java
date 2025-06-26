package com.zayaanit.todoist.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.zayaanit.todoist.enums.ResponseStatusType;
import com.zayaanit.todoist.model.ResponseBuilder;
import com.zayaanit.todoist.model.SuccessResponse;
import com.zayaanit.todoist.service.BaseService;

import lombok.RequiredArgsConstructor;

/**
 * Zubayer Ahamed
 * @since Jun 26, 2025
 */
@RequiredArgsConstructor
public class AbstractBaseController<LIST, REQ, RES> implements BaseController<LIST, REQ, RES> {

	protected final BaseService<LIST, REQ, RES> service;

	@GetMapping
	@Override
	public ResponseEntity<SuccessResponse<LIST>> getAll() {
		LIST data = service.getAll();
		return ResponseBuilder.build(ResponseStatusType.READ_SUCCESS, data);
	}

	@PostMapping
	@Override
	public ResponseEntity<SuccessResponse<RES>> save(REQ req) {
		RES data = service.save(req);
		return ResponseBuilder.build(ResponseStatusType.CREATE_SUCCESS, data);
	}

	@PutMapping
	@Override
	public ResponseEntity<SuccessResponse<RES>> update(REQ req) {
		RES data = service.update(req);
		return ResponseBuilder.build(ResponseStatusType.UPDATE_SUCCESS, data);
	}

	@GetMapping("/{id}")
	@Override
	public ResponseEntity<SuccessResponse<RES>> find(Object id) {
		RES data = service.find(id);
		return ResponseBuilder.build(ResponseStatusType.READ_SUCCESS, data);
	}

	@DeleteMapping("/{id}")
	@Override
	public ResponseEntity<SuccessResponse<RES>> delete(Object id) {
		service.delete(id);
		return ResponseBuilder.build(ResponseStatusType.DELETE_NO_CONTENT, null);
	}

}
