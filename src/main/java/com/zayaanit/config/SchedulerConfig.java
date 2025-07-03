package com.zayaanit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * Zubayer Ahamed
 * 
 * @since Jul 3, 2025
 */
@Configuration
public class SchedulerConfig {

//	@Bean
//	TaskScheduler taskScheduler() {
//		return new ConcurrentTaskScheduler(); // Can be ThreadPoolTaskScheduler too
//	}

	@Bean
	ThreadPoolTaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setPoolSize(10); // tune based on load
		scheduler.setThreadNamePrefix("task-reminder-");
		return scheduler;
	}
}
