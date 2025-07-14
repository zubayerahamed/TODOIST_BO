package com.zayaanit.module.tasks;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zayaanit.enums.PriorityType;
import com.zayaanit.enums.TaskType;
import com.zayaanit.module.tasks.subtasks.SubTask;

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
	private Boolean isReminderEnabled;

	private List<Long> tags;
	private List<Long> perticipants;
	private List<SubTaskReqDto> subTasks;
	private List<Long> files;

	public Task getBean() {
		return Task.builder()
				.taskType(taskType)
				.title(title)
				.description(description)
				.projectId(projectId)
				.sectionId(sectionId)
				.categoryId(categoryId)
				.workflowId(workflowId)
				.taskDate(taskDate)
				.taskStartTime(taskStartTime == null ? LocalTime.MIDNIGHT : taskStartTime)
				.estTime(estTime)
				.dueDate(dueDate)
				.taskEndTime(taskEndTime == null ? LocalTime.of(23, 59) : taskEndTime)
				.priority(priority)
				
				
				.isReminderEnabled(taskType.equals(TaskType.EVENT) ? isReminderEnabled : false)
				.reminderBefore(taskType.equals(TaskType.EVENT) && Boolean.TRUE.equals(isReminderEnabled) ? (reminderBefore == null ? 0 : reminderBefore) : 0)
				
				.location(location)
				.build();
	}
}

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class SubTaskReqDto{
	private Long userId;
	private String title;

	public SubTask getBean() {
		return SubTask.builder()
				.userId(userId)
				.title(title)
				.build();
	}
}
