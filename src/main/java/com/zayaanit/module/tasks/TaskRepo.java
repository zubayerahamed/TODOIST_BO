package com.zayaanit.module.tasks;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {

	@Query(value = """
			SELECT * 
			FROM tasks 
			WHERE is_reminder_sent = 0 
			  AND DATEADD(SECOND, 
			              DATEDIFF(SECOND, 0, task_start_time), 
			              CAST(task_date AS DATETIME)
			          ) >= GETDATE()
		""", nativeQuery = true)
	List<Task> findAllPendingReminders();

	List<Task> findAllByProjectId(Long projectId);

	@Query("SELECT COUNT(e) FROM Task e WHERE e.projectId=:projectId AND e.isCompleted = false")
	long countProjectActiveTasks(@Param("projectId") Long projectId);
}
