package com.zayaanit.module.tasks.subtasks;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Zubayer Ahamed
 * 
 * @since Jul 13, 2025
 */
@Repository
public interface SubTaskRepo extends JpaRepository<SubTask, Long> {

	List<SubTask> findAllByTaskId(Long taskId);

	List<SubTask> findAllByUserId(Long userId);
}
