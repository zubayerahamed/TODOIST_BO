package com.zayaanit.module.tasks;

import java.time.LocalDate;

import org.springframework.beans.BeanUtils;

import com.zayaanit.enums.PriorityType;

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
	private String title;
	private String description;
	private Long projectId;
	private Long sectionId;
	private Long categoryId;
	private Long workflowId;
	private LocalDate taskDate;
	private LocalDate dueDate;
	private Integer estTime;
	private PriorityType priority;
	private Boolean isCompleted;

	public TaskResDto(Task obj) {
		BeanUtils.copyProperties(obj, this);
	}

	public static TaskResDto from(Task obj) {
		TaskResDto dto = new TaskResDto();
		BeanUtils.copyProperties(obj, dto);
		return dto;
	}
}
