package com.zayaanit.module.users.workspaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserWorkspaceRepo extends JpaRepository<UserWorkspace, UsersWorkspacesPK> {

	List<UserWorkspace> findAllByUserId(Long userId);
	Optional<UserWorkspace> findByUserIdAndIsPrimary(Long userId, Boolean isPrimary);
	Optional<UserWorkspace> findByUserIdAndWorkspaceId(Long userId, Long workspaceId);
	Optional<UserWorkspace> findByUserIdAndIsAdmin(Long userId, Boolean isAdmin);
	Optional<UserWorkspace> findByUserIdAndIsCollaborator(Long userId, Boolean isCollaborator);
}
