package com.zayaanit.module.category;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.enums.ReferenceType;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {

	List<Category> findAllByReferenceIdAndReferenceType(Long referenceId, ReferenceType referenceType);
	List<Category> findAllByReferenceIdAndReferenceTypeAndIsInheritedTrue(Long referenceId, ReferenceType referenceType);
	List<Category> findAllByReferenceIdAndReferenceTypeAndIsInheritedFalse(Long referenceId, ReferenceType referenceType);

	Optional<Category> findByReferenceIdAndReferenceTypeAndIsDefaultForTaskTrue(Long referenceId, ReferenceType referenceType);
	Optional<Category> findByReferenceIdAndReferenceTypeAndIsDefaultForTaskFalse(Long referenceId, ReferenceType referenceType);
	Optional<Category> findByReferenceIdAndReferenceTypeAndIsDefaultForTaskTrueAndIsInheritedTrue(Long referenceId, ReferenceType referenceType);
	Optional<Category> findByReferenceIdAndReferenceTypeAndIsDefaultForTaskTrueAndIsInheritedFalse(Long referenceId, ReferenceType referenceType);

	Optional<Category> findByReferenceIdAndReferenceTypeAndIsDefaultForEventTrue(Long referenceId, ReferenceType referenceType);
	Optional<Category> findByReferenceIdAndReferenceTypeAndIsDefaultForEventFalse(Long referenceId, ReferenceType referenceType);
	Optional<Category> findByReferenceIdAndReferenceTypeAndIsDefaultForEventTrueAndIsInheritedTrue(Long referenceId, ReferenceType referenceType);
	Optional<Category> findByReferenceIdAndReferenceTypeAndIsDefaultForEventTrueAndIsInheritedFalse(Long referenceId, ReferenceType referenceType);

	Optional<Category> findByReferenceIdAndReferenceTypeAndParentId(Long referenceId, ReferenceType referenceType, Long parentId);

	List<Category> findAllByParentIdAndIsInheritedTrue(Long parentId);
}
