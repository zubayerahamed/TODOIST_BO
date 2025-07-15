package com.zayaanit.module.notification;

/**
 * Zubayer Ahamed
 * 
 * @since Jul 15, 2025
 */
public enum NotificationType {

	EVENT_CREATED("TasksNest - A New Event Has Been Created"),
	EVENT_UPDATED("TasksNest - Event Updated Notification"),
	EVENT_DELETED("TasksNest - Event Cancelled/Deleted"),
	EVENT_ASSIGNED("TasksNest - You’ve Been Assigned a New Event"),
	EVENT_UNASSIGNED("TasksNest - You’ve Been Unassigned from an Event"),
	EVENT_REMINDER("TasksNest - Event Reminder: Don't Miss Your Upcoming Event!"),
	EVENT_COMPLETED("TasksNest - Event Completed");

	private String title;

	NotificationType(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}
}
