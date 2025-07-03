package com.zayaanit.todoist.controller.category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {

	List<Category> findAllByReferenceId(Long referenceId);
}
