package com.zayaanit.todoist.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.todoist.entity.Workspaces;

@Repository
public interface WorkspacesRepo extends JpaRepository<Workspaces, Long> {

}
