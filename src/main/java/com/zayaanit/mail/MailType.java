package com.zayaanit.mail;

/**
 * Zubayer Ahamed
 * @since Jul 13, 2025
 */
public enum MailType {

	EVENT_CREATED(
		"TasksNest - A New Event Has Been Created",
		"src/main/resources/static/standard_event_created_email_template.vm"
	), 

	EVENT_UPDATED(
		"TasksNest - Event Updated Notification",
		"src/main/resources/static/standard_event_updated_email_template.vm"
	), 

	EVENT_DELETED(
		"TasksNest - Event Cancelled/Deleted",
		"src/main/resources/static/standard_event_deleted_email_template.vm"
	), 

	EVENT_ASSIGNED(
		"TasksNest - You’ve Been Assigned a New Event",
		"src/main/resources/static/standard_event_assigend_email_template.vm"
	), 

	EVENT_UNASSIGNED(
		"TasksNest - You’ve Been Unassigned from an Event",
		"src/main/resources/static/standard_event_unassigned_email_template.vm"
	), 

	EVENT_REMINDER(
		"TasksNest - Event Reminder: Don't Miss Your Upcoming Event!",
		"src/main/resources/static/standard_event_reminder_email_template.vm"
	);

	private String subject;
	private String template;

	MailType(String subject, String template) {
		this.subject = subject;
		this.template = template;
	}

	public String getSubject() {
		return this.subject;
	}

	public String getTemplate() {
		return this.template;
	}
}
