package com.zayaanit.module.reminder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import com.zayaanit.module.events.Event;

/**
 * Zubayer Ahamed
 * 
 * @since Jul 3, 2025
 */
@Service
public class ReminderService {

	private final TaskScheduler taskScheduler;
	private final Map<Long, ScheduledFuture<?>> scheduledEvents = new ConcurrentHashMap<>();

	public ReminderService(TaskScheduler taskScheduler) {
		this.taskScheduler = taskScheduler;
	}

	public void scheduleEventReminder(Event event, Runnable reminderCallbackMethod) {
		LocalDateTime dateTime = LocalDateTime.of(event.getEventDate(), event.getStartTime()).minusMinutes(event.getReminderBefore());
		Date reminderTime = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());

		// Cancel previous if re-scheduling
		cancelScheduledReminder(event.getId());

		ScheduledFuture<?> future = taskScheduler.schedule(() -> {
			try {
				reminderCallbackMethod.run();
			} finally {
				cancelScheduledReminder(event.getId());
			}
		}, reminderTime.toInstant());

		scheduledEvents.put(event.getId(), future);
	}

	public void cancelScheduledReminder(Long taskId) {
		if(scheduledEvents == null || scheduledEvents.get(taskId) == null) return;
		ScheduledFuture<?> future = scheduledEvents.get(taskId);
		if (future != null && !future.isDone()) {
			future.cancel(true);
			scheduledEvents.remove(taskId);
		}
	}

}
