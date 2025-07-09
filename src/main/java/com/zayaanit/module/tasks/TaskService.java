package com.zayaanit.module.tasks;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.exception.CustomException;
import com.zayaanit.module.reminder.ReminderService;

import jakarta.transaction.Transactional;

/**
 * Zubayer Ahamed
 * @since Jul 3, 2025
 */
@Service
public class TaskService {

	@Autowired private TaskRepo taskRepo;
	@Autowired private ReminderService reminderService;

	@Transactional
	public CreateTaskResDto create(CreateTaskReqDto reqDto) throws CustomException {
		Task task = reqDto.getBean();
		task.setIsCompleted(false);
		task.setIsPartiallyCompleted(false);
		task.setIsReminderSent(false);
		Task finaltask = taskRepo.save(task);

		// After creating task, if it is event type, then schedule it for reminder
		reminderService.scheduleReminder(task, () -> {
			System.out.println("Reminder for task : " + finaltask.getTitle());
		});

		return new CreateTaskResDto(task);
	}

	public void rescheduleAllReminders() {
		List<Task> futureTasks = taskRepo.findAllPendingReminders();
		futureTasks.forEach(t -> {
			reminderService.scheduleReminder(t, () -> {
				// send email
				// send push notification
				// send sms
				
				// update task reminder send in db
			});
		});
	}
}
