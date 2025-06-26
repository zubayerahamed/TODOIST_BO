package com.zayaanit.todoist.service;

import com.zayaanit.todoist.exception.CustomException;

/**
 * Zubayer Ahamed
 * @since Jun 26, 2025
 */
public interface BaseService<LIST, REQ, RES> {

	LIST getAll() throws CustomException;
	RES save(REQ req) throws CustomException;
	RES update(REQ req) throws CustomException;
	RES find(Object id) throws CustomException;
	void delete(Object id) throws CustomException;
}
