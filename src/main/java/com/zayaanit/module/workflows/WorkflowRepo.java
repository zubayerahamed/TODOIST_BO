package com.zayaanit.module.workflows;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.enums.ReferenceType;

@Repository
public interface WorkflowRepo extends JpaRepository<Workflow, Long> {

	List<Workflow> findAllByReferenceIdAndReferenceType(Long referenceId, ReferenceType referenceType);
}
