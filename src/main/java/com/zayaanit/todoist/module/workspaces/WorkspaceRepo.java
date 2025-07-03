package com.zayaanit.todoist.module.workspaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkspaceRepo extends JpaRepository<Workspace, Long> {

}
