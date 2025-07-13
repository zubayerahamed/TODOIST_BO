package com.zayaanit.module.sections;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionRepo extends JpaRepository<Section, Long> {
	List<Section> findAllByProjectId(Long projectId);
}
