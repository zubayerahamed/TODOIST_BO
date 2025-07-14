package com.zayaanit.module.reminder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zayaanit.module.events.EventService;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/**
 * Zubayer Ahamed
 * @since Jul 3, 2025
 */
@Slf4j
@Component
public class ReminderStartupScheduler {

	@Autowired private EventService eventkService;

	@PostConstruct
	public void rescheduleFutureTasks() {
		log.info("======> Start reshedule all pending reminders <======");
		eventkService.rescheduleAllEventReminders();
	}
}
