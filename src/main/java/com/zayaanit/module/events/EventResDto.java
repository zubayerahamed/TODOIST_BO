package com.zayaanit.module.events;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.BeanUtils;

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
public class EventResDto {

	private Long id;
	private String title;
	private String description;
	private Long projectId;
	private Long categoryId;
	private LocalDate eventDate;
	private LocalTime startTime;
	private LocalTime endTime;
	private String location;
	private Boolean isReminderEnabled;
	private Integer reminderBefore; // How many minutes before to send reminder
	private Boolean isReminderSent;
	private Boolean isCompleted;

	public EventResDto(Event obj) {
		BeanUtils.copyProperties(obj, this);
	}

	public static EventResDto from(Event obj) {
		EventResDto dto = new EventResDto();
		BeanUtils.copyProperties(obj, dto);
		return dto;
	}
}
