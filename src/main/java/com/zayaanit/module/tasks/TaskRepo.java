package com.zayaanit.module.tasks;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {

	@Query("SELECT t FROM Task t WHERE t.isReminderSent = false AND t.taskDate >= CURRENT_DATE")
	List<Task> findAllPendingReminders();
}
