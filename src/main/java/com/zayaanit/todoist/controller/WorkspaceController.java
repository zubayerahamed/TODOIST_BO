package com.zayaanit.todoist.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;

import com.zayaanit.todoist.anno.RestApiController;
import com.zayaanit.todoist.dto.req.WorkspaceReqDto;
import com.zayaanit.todoist.dto.res.WorkspaceResDto;
import com.zayaanit.todoist.service.WorkspaceService;

/**
 * Zubayer Ahamed
 * @since Jun 26, 2025
 */
@RestApiController
@RequestMapping("/api/v1/workspaces")
public class WorkspaceController extends AbstractBaseController<List<WorkspaceResDto>, WorkspaceReqDto, WorkspaceResDto> {

	public WorkspaceController(WorkspaceService<List<WorkspaceResDto>, WorkspaceReqDto, WorkspaceResDto> service) {
		super(service);
	}

}
