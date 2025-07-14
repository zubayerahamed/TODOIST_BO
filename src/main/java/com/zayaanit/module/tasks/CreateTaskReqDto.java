package com.zayaanit.module.tasks;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zayaanit.enums.PriorityType;
import com.zayaanit.module.tasks.subtasks.CreateSubTaskReqDto;

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

	@NotBlank(message = "Title required")
	@Size(min = 1, max = 100, message = "Title must be 1 to 100 characters long")
	private String title;
	private String description;

	@NotNull(message = "Project reference required")
	private Long projectId;
	private Long sectionId;
	private Long categoryId;
	private Long workflowId;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate taskDate;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate dueDate;
	private Integer estTime;
	private PriorityType priority;

	private List<Long> tags;
	private List<Long> documents;
	private List<Long> perticipants;
	private List<CreateSubTaskReqDto> subTasks;

	public Task getBean() {
		return Task.builder()
				.title(title)
				.description(description)
				.projectId(projectId)
				.sectionId(sectionId)
				.categoryId(categoryId)
				.workflowId(workflowId)
				.taskDate(taskDate)
				.estTime(estTime)
				.dueDate(dueDate)
				.priority(priority)
				.isCompleted(false)
				.build();
	}
}
