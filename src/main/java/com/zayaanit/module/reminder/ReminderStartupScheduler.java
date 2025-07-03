package com.zayaanit.module.reminder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

/**
 * Zubayer Ahamed
 * @since Jul 3, 2025
 */
@Component
public class ReminderStartupScheduler {

	@Autowired private ReminderService reminderService;

	@PostConstruct
    public void rescheduleFutureTasks() {
		System.out.println("======> Running this auto schedular");
//        List<Task> futureTasks = taskRepository.findAllTasksWithReminderInFuture();
//        for (Task task : futureTasks) {
//            reminderService.scheduleReminder(task);
//        }
    }
}
