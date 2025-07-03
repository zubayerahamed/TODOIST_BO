package com.zayaanit.module.tasks;

import java.util.Date;

import com.zayaanit.enums.PriorityType;
import com.zayaanit.model.AbstractModel;

import jakarta.persistence.Column;
/**
 * Zubayer Ahamed
 * @since Jul 3, 2025
 */
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")
@EqualsAndHashCode(callSuper = true)
public class Task extends AbstractModel<Long> {

	private static final long serialVersionUID = 2572842083641054917L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "project_id", nullable = false)
	private Long projectId;

	@Column(name = "section_id", nullable = true)
	private Long sectionId;

	@Column(name = "category_id", nullable = true)
	private Long categoryId;

	@Column(name = "workflow_id", nullable = true)
	private Long workflowId;

	@Column(name = "title", length = 100)
	private String title;

	@Lob
	@Column(name = "description")
	private String description;

	@Column(name = "is_type_task", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean isTypeTask;

	@Column(name = "is_type_event", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean isTypeEvent;

	@Temporal(TemporalType.DATE)
	@Column(name = "task_date")
	private Date taskDate; // Target user email for notification

	@Temporal(TemporalType.TIME)
	@Column(name = "task_start_time")
	private Date taskStartTime;

	@Temporal(TemporalType.TIME)
	@Column(name = "task_end_time")
	private Date taskEndTime;

	@Temporal(TemporalType.DATE)
	@Column(name = "due_date")
	private Date dueDate;

	@Column(name = "estTime")
	private Integer estTime; // In minute

	@Column(name = "reminder_before")
	private Integer reminderBefore; // How many minutes before to send reminder

	@Enumerated(EnumType.STRING)
	@Column(name = "priority")
	private PriorityType priority;

	@Column(name = "location", length = 50)
	private String location;

	@Column(name = "is_reminder_sent", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean isReminderSent;

	@Column(name = "is_completed", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean isCompleted;
}