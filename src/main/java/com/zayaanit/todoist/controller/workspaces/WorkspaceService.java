package com.zayaanit.todoist.controller.workspaces;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.zayaanit.todoist.exception.CustomException;

/**
 * Zubayer Ahamed
 * @since Jun 22, 2025
 */
@Service
public class WorkspaceService {

	@Autowired private WorkspaceRepo workspaceRepo;

	public Workspace findById(Long zid) throws CustomException {
		if(zid == null) throw new CustomException("Workspace id null", HttpStatus.BAD_REQUEST);

		Optional<Workspace> workspaceOp = workspaceRepo.findById(zid);

		return workspaceOp.isPresent() ? workspaceOp.get() : null;
	}
}
