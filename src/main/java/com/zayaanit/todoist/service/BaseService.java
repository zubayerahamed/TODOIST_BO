package com.zayaanit.todoist.service;

import org.springframework.data.domain.Pageable;

import com.zayaanit.todoist.exception.CustomException;

/**
 * Zubayer Ahamed
 * @since Jun 26, 2025
 */
public interface BaseService<REQ, RES, LIST, PAGE, ID> {

	LIST getAll() throws CustomException;
	PAGE getAll(Pageable pageable) throws CustomException;
	RES save(REQ req) throws CustomException;
	RES update(REQ req) throws CustomException;
	RES find(ID id) throws CustomException;
	void delete(ID id) throws CustomException;
}
