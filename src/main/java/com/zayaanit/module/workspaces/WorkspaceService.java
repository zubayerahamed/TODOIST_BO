package com.zayaanit.module.workspaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.zayaanit.exception.CustomException;
import com.zayaanit.module.BaseService;
import com.zayaanit.module.users.workspaces.UserWorkspace;
import com.zayaanit.module.users.workspaces.UserWorkspaceRepo;
import com.zayaanit.module.users.workspaces.UsersWorkspacesPK;

import io.jsonwebtoken.lang.Collections;
import jakarta.transaction.Transactional;

/**
 * Zubayer Ahamed
 * @since Jun 22, 2025
 */
@Service
public class WorkspaceService extends BaseService {

	@Autowired private WorkspaceRepo workspaceRepo;
	@Autowired private UserWorkspaceRepo userWorkspaceRepo;

	public List<WorkspaceResDto> getAll(){
		List<UserWorkspace> userWorkspaces = userWorkspaceRepo.findAllByUserId(loggedinUser().getUserId());
		if(userWorkspaces.isEmpty()) return Collections.emptyList();

		List<WorkspaceResDto> workspacesResponse = new ArrayList<>();
		userWorkspaces.stream().forEach(uw -> {
			Optional<Workspace> workspaceOp = workspaceRepo.findById(uw.getWorkspaceId());
			if(workspaceOp.isPresent()) {
				workspacesResponse.add(new WorkspaceResDto(workspaceOp.get(), uw));
			}
		});

		return workspacesResponse;
	}

	public WorkspaceResDto findById(Long id) throws CustomException {
		Optional<UserWorkspace> userWorkspaceOp = userWorkspaceRepo.findById(new UsersWorkspacesPK(loggedinUser().getUserId(), id));
		if(!userWorkspaceOp.isPresent()) throw new CustomException("User has no access with this workspace", HttpStatus.UNAUTHORIZED);

		Optional<Workspace> workspaceOp = workspaceRepo.findById(id);
		if(!workspaceOp.isPresent()) throw new CustomException("Workspace not exist", HttpStatus.NOT_FOUND);
		return new WorkspaceResDto(workspaceOp.get(), userWorkspaceOp.get());
	}

	@Transactional
	public CreateWorkspaceResDto create(CreateWorkspaceReqDto reqDto) {
		Workspace workspace = reqDto.getBean();
		workspace.setIsActive(true);
		workspace.setIsSystemDefined(false);
		workspace.setLogo(null);
		workspace = workspaceRepo.save(workspace);

		// Now make relation workspace with user
		UserWorkspace userWorkpsace = UserWorkspace.builder()
				.userId(loggedinUser().getUserId())
				.workspaceId(workspace.getId())
				.isPrimary(false)
				.isAdmin(true)
				.isCollaborator(false)
				.build();

		userWorkspaceRepo.save(userWorkpsace);

		return new CreateWorkspaceResDto(workspace, userWorkpsace);
	}

	@Transactional
	public UpdateWorkspaceResDto update(UpdateWorkspaceReqDto reqDto) {
		Optional<UserWorkspace> userWorkspaceOp = userWorkspaceRepo.findById(new UsersWorkspacesPK(loggedinUser().getUserId(), reqDto.getId()));
		if(!userWorkspaceOp.isPresent()) throw new CustomException("User has no access with this workspace", HttpStatus.UNAUTHORIZED);

		if(Boolean.TRUE.equals(userWorkspaceOp.get().getIsCollaborator())) {
			throw new CustomException("You are not admin of this workspace", HttpStatus.UNAUTHORIZED);
		}

		Optional<Workspace> workspaceOp = workspaceRepo.findById(reqDto.getId());
		if(!workspaceOp.isPresent()) throw new CustomException("Workspace not exist", HttpStatus.NOT_FOUND);

		Workspace existobj = workspaceOp.get();
		BeanUtils.copyProperties(reqDto, existobj);
		existobj = workspaceRepo.save(existobj);
		return new UpdateWorkspaceResDto(existobj, userWorkspaceOp.get());
	}

	@Transactional
	public void deleteById(Long id) throws CustomException {
		Optional<UserWorkspace> userWorkspaceOp = userWorkspaceRepo.findById(new UsersWorkspacesPK(loggedinUser().getUserId(), id));
		if(!userWorkspaceOp.isPresent()) throw new CustomException("User has no access with this workspace", HttpStatus.UNAUTHORIZED);

		if(Boolean.TRUE.equals(userWorkspaceOp.get().getIsPrimary())) {
			throw new CustomException("Primary workspace can't be deleted.", HttpStatus.BAD_REQUEST);
		}

		if(Boolean.TRUE.equals(userWorkspaceOp.get().getIsCollaborator())) {
			throw new CustomException("You are not admin of this workspace", HttpStatus.UNAUTHORIZED);
		}

		Optional<Workspace> workspaceOp = workspaceRepo.findById(id);
		if(!workspaceOp.isPresent()) throw new CustomException("Workspace not exist", HttpStatus.NOT_FOUND);

		// TODO: need to delte everything from database related to it
		workspaceRepo.delete(workspaceOp.get());
	}
}
