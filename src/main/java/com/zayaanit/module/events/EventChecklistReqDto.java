package com.zayaanit.module.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Zubayer Ahamed
 * @since Aug 27, 2025
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventChecklistReqDto {

	private String description;
	private Boolean isCompleted;

	public EventChecklist getBean() {
		return EventChecklist.builder()
				.description(description)
				.isCompleted(isCompleted)
				.build();
	}
}
