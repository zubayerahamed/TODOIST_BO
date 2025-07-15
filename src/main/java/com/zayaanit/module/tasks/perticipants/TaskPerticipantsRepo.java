package com.zayaanit.module.tasks.perticipants;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskPerticipantsRepo extends JpaRepository<TaskPerticipants, TaskPerticipantsPK> {

	List<TaskPerticipants> findAllByTaskId(Long taskId);

	void deleteAllByTaskId(Long taskId);
}
