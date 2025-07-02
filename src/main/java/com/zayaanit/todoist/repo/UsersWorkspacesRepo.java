package com.zayaanit.todoist.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.todoist.entity.UsersWorkspaces;
import com.zayaanit.todoist.entity.pk.UsersWorkspacesPK;

@Repository
public interface UsersWorkspacesRepo extends JpaRepository<UsersWorkspaces, UsersWorkspacesPK> {

	List<UsersWorkspaces> findAllByUserId(Long userId);
	Optional<UsersWorkspaces> findByUserIdAndIsPrimary(Long userId, Boolean isPrimary);
	Optional<UsersWorkspaces> findByUserIdAndIsAdmin(Long userId, Boolean isAdmin);
	Optional<UsersWorkspaces> findByUserIdAndIsCollaborator(Long userId, Boolean isCollaborator);
}
