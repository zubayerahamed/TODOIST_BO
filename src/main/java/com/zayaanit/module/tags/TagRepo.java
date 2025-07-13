package com.zayaanit.module.tags;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepo extends JpaRepository<Tag, Long> {

	Optional<Tag> findByIdAndWorkspaceId(Long id, Long workspaceId);

	List<Tag> findAllByWorkspaceId(Long workspaceId);

}
