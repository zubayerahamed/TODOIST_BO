package com.zayaanit.module.category;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.enums.ReferenceType;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {

	List<Category> findAllByReferenceIdAndReferenceType(Long referenceId, ReferenceType referenceType);

	Optional<Category> findByReferenceIdAndReferenceTypeAndIsDefaultForTask(Long referenceId, ReferenceType referenceType, Boolean isDefaultForTask);
	Optional<Category> findByReferenceIdAndReferenceTypeAndIsDefaultForEvent(Long referenceId, ReferenceType referenceType, Boolean isDefaultForEvent);
}
