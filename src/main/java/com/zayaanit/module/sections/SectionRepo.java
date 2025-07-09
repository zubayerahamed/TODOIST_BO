package com.zayaanit.module.sections;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionRepo extends JpaRepository<Section, Long> {

    Optional<Section> findByIdAndProjectId(Long id, Long projectId);

    List<Section> findAllByProjectId(Long projectId);
}
