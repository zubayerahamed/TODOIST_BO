package com.zayaanit.module.tasks.subtasks;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Zubayer Ahamed
 * @since Jul 14, 2025
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSubTaskReqDto {

	private Long id;
	private Long taskId;
	private Long userId;
	private String title;

	public SubTask getBean() {
		return SubTask.builder()
				.id(id == null ? -1 : id)
				.taskId(taskId == null ? -1 : taskId)
				.userId(userId)
				.title(title)
				.build();
	}
}
