package com.zayaanit.module.events.parents;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.BeanUtils;

import com.zayaanit.module.events.Event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateParentEventReqDto{


	private String title;
	private String description;
	private Long projectId;
	private Long categoryId;
	private LocalDate eventDate;
	private LocalTime startTime;
	private LocalTime endTime;
	private String location;
	private String eventLink;
	private Boolean isReminderEnabled;
	private Integer reminderBefore;

	public static CreateParentEventReqDto from(Event obj) {
		CreateParentEventReqDto dto = new CreateParentEventReqDto();
		BeanUtils.copyProperties(obj, dto);
		return dto;
	}

	public ParentEvent getBean() {
		return ParentEvent.builder()
				.title(title)
				.description(description)
				.projectId(projectId)
				.categoryId(categoryId)
				.eventDate(eventDate)
				.startTime(startTime == null ? LocalTime.MIDNIGHT : startTime)
				.endTime(endTime == null ? LocalTime.of(23, 59) : endTime)
				.location(location)
				.eventLink(eventLink)
				.isReminderEnabled(reminderBefore != null)
				.reminderBefore(reminderBefore)
				.build();
	}
}