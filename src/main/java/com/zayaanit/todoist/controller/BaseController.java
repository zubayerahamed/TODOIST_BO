package com.zayaanit.todoist.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import com.zayaanit.todoist.model.SuccessResponse;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

/**
 * Zubayer Ahamed
 * @since Jun 26, 2025
 */
public interface BaseController<LIST, REQ, RES> {

	ResponseEntity<SuccessResponse<LIST>> getAll();
	ResponseEntity<SuccessResponse<RES>> save(@RequestBody REQ req);
	ResponseEntity<SuccessResponse<RES>> update(@RequestBody REQ req);
	ResponseEntity<SuccessResponse<RES>> find(@PathVariable Object id);
	ResponseEntity<SuccessResponse<RES>> delete(@PathVariable Object id);
}
