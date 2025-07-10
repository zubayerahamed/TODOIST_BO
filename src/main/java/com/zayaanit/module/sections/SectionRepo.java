package com.zayaanit.module.sections;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionRepo extends JpaRepository<Section, Long> {

    Optional<Section> findById(Long id);

    List<Section> findAllByProjectId(Long projectId);
}
