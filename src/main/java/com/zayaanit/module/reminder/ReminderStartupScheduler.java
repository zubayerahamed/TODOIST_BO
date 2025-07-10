package com.zayaanit.module.reminder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zayaanit.module.tasks.TaskService;

import jakarta.annotation.PostConstruct;

/**
 * Zubayer Ahamed
 * @since Jul 3, 2025
 */
@Component
public class ReminderStartupScheduler {

	@Autowired private TaskService taskService;

	@PostConstruct
	public void rescheduleFutureTasks() {
		System.out.println("======> Running this auto schedular for reshedule all pending reminder on startuup the program");
		taskService.rescheduleAllReminders();
	}
}
