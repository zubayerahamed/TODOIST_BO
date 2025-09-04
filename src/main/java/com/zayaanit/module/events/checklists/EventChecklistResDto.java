package com.zayaanit.module.events.checklists;

import org.springframework.beans.BeanUtils;

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
public class EventChecklistResDto {

	private Long id;
	private Long eventId;
	private String description;
	private Boolean isCompleted;

	public EventChecklistResDto(EventChecklist obj) {
		BeanUtils.copyProperties(obj, this);
	}
}
