package com.zayaanit.module.workflows;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.enums.ReferenceType;

@Repository
public interface WorkflowRepo extends JpaRepository<Workflow, Long> {

	List<Workflow> findAllByReferenceIdAndReferenceType(Long referenceId, ReferenceType referenceType);
	List<Workflow> findAllByReferenceIdAndReferenceTypeAndIsInheritedTrue(Long referenceId, ReferenceType referenceType);
	List<Workflow> findAllByReferenceIdAndReferenceTypeAndIsInheritedFalse(Long referenceId, ReferenceType referenceType);

	Optional<Workflow> findByReferenceIdAndReferenceTypeAndIsSystemDefinedTrue(Long referenceId, ReferenceType referenceType);
	List<Workflow> findAllByReferenceIdAndReferenceTypeAndIsSystemDefinedFalse(Long referenceId, ReferenceType referenceType);

	Optional<Workflow> findByReferenceIdAndReferenceTypeAndParentId(Long referenceId, ReferenceType referenceType, Long parentId);
}
