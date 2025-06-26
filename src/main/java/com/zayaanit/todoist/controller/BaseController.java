package com.zayaanit.todoist.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.zayaanit.todoist.model.SuccessResponse;

/**
 * Zubayer Ahamed
 * @since Jun 26, 2025
 */
public interface BaseController<REQ, RES, LIST, PAGE, ID> {

	ResponseEntity<SuccessResponse<LIST>> getAll();
	ResponseEntity<SuccessResponse<PAGE>> getAll(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(required = true) String sortBy,
			@RequestParam(defaultValue = "true") boolean ascending);
	ResponseEntity<SuccessResponse<RES>> save(@RequestBody REQ req);
	ResponseEntity<SuccessResponse<RES>> update(@RequestBody REQ req);
	ResponseEntity<SuccessResponse<RES>> find(@RequestParam ID id);
	ResponseEntity<SuccessResponse<RES>> delete(@RequestParam ID id);
}
