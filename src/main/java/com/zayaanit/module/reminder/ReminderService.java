package com.zayaanit.module.reminder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import com.zayaanit.module.tasks.Task;

/**
 * Zubayer Ahamed
 * 
 * @since Jul 3, 2025
 */
@Service
public class ReminderService {

	private final TaskScheduler taskScheduler;
	private final Map<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

	public ReminderService(TaskScheduler taskScheduler) {
		this.taskScheduler = taskScheduler;
	}

	public void scheduleReminder(Task task, Runnable reminderCallbackMethod) {
		LocalDateTime dateTime = LocalDateTime.of(task.getTaskDate(), task.getTaskStartTime()).minusMinutes(task.getReminderBefore());
		Date reminderTime = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());

		// Cancel previous if re-scheduling
		cancelScheduledReminder(task.getId());

		ScheduledFuture<?> future = taskScheduler.schedule(reminderCallbackMethod, reminderTime.toInstant());
		scheduledTasks.put(task.getId(), future);
	}

	public void cancelScheduledReminder(Long taskId) {
		if(scheduledTasks == null || scheduledTasks.get(taskId) == null) return;
		ScheduledFuture<?> future = scheduledTasks.get(taskId);
		if (future != null && !future.isDone()) {
			future.cancel(true);
		}
	}

}
