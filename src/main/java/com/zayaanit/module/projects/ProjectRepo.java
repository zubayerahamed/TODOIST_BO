package com.zayaanit.module.projects;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepo extends JpaRepository<Project, Long> {

	Optional<Project> findByIdAndWorkspaceId(Long id, Long workspaceId);
	List<Project> findAllByWorkspaceId(Long workspaceId);
	Page<Project> findAllByWorkspaceId(Long workspaceId, Pageable pageable);
}
