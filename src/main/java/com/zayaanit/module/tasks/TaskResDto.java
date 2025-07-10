package com.zayaanit.module.tasks;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.BeanUtils;

import com.zayaanit.enums.PriorityType;
import com.zayaanit.enums.TaskType;

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
public class TaskResDto {

	private Long id;
	private Long projectId;
	private Long sectionId;
	private Long categoryId;
	private Long workflowId;
	private String title;
	private String description;
	private TaskType taskType;
	private LocalDate taskDate;
	private LocalTime taskStartTime;
	private LocalTime taskEndTime;
	private LocalDate dueDate;
	private Integer estTime;
	private Integer reminderBefore;
	private PriorityType priority;
	private String location;
	private Boolean isReminderSent;
	private Boolean isCompleted;
	private Boolean isPartiallyCompleted;

	public TaskResDto(Task obj) {
		BeanUtils.copyProperties(obj, this);
	}

	public static TaskResDto from(Task obj) {
		TaskResDto dto = new TaskResDto();
		BeanUtils.copyProperties(obj, dto);
		return dto;
	}
}
