package com.zayaanit.module.tasks;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zayaanit.enums.PriorityType;
import com.zayaanit.enums.TaskType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Zubayer Ahamed
 * @since Jul 3, 2025
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateTaskReqDto {

	@NotNull(message = "Project reference required")
	private Long projectId;
	private Long sectionId;
	private Long categoryId;
	private Long workflowId;
	@NotBlank(message = "Title required")
	@Size(min = 1, max = 100, message = "Title must be 1 to 100 characters long")
	private String title;
	private String description;
	@NotNull(message = "Task type required")
	private TaskType taskType;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate taskDate;
	@JsonFormat(pattern = "HH:mm")
	private LocalTime taskStartTime;
	@JsonFormat(pattern = "HH:mm")
	private LocalTime taskEndTime;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate dueDate;
	private Integer estTime;
	private Integer reminderBefore;
	private PriorityType priority;
	private String location;
	private boolean isReminderEnabled;

	public Task getBean() {
		return Task.builder()
				.projectId(projectId)
				.sectionId(sectionId)
				.categoryId(categoryId)
				.workflowId(workflowId)
				.title(title)
				.description(description)
				.taskType(taskType)
				.taskDate(taskDate)
				.taskStartTime(taskStartTime)
				.taskEndTime(taskEndTime)
				.dueDate(dueDate)
				.estTime(estTime)
				.isReminderEnabled(taskType.equals(TaskType.EVENT) ? isReminderEnabled : false)
				.reminderBefore(taskType.equals(TaskType.EVENT) && isReminderEnabled ? (reminderBefore == null ? 0 : reminderBefore) : 0)
				.priority(priority)
				.location(location)
				.build();
	}
}
