package com.zayaanit.module.tasks.tags;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskTagRepo extends JpaRepository<TaskTag, TaskTagPK> {

	List<TaskTag> findAllByTaskId(Long taskId);

	List<TaskTag> findAllByTagId(Long tagId);

	void deleteAllByTaskId(Long taskId);
}
