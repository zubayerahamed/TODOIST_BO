package com.zayaanit.module.workflows;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowRepo extends JpaRepository<Workflow, Long> {

}
