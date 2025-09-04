package com.zayaanit.module.events;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zayaanit.module.events.checklists.CreateEventChecklistReqDto;

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
@ValidCreateEventRequest
public class CreateEventReqDto {

	@NotBlank(message = "Title required")
	@Size(min = 1, max = 100, message = "Title must be 1 to 100 characters long")
	private String title;
	private String description;

	@NotNull(message = "Project reference required")
	private Long projectId;
	private Long categoryId;

	@NotNull(message = "Event date required")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate eventDate;
	@NotNull(message = "Event start time required")
	@JsonFormat(pattern = "HH:mm")
	private LocalTime startTime;
	@NotNull(message = "Event end time required")
	@JsonFormat(pattern = "HH:mm")
	private LocalTime endTime;

	private String location;
	private Boolean isReminderEnabled;
	private Integer reminderBefore;
	private String eventLink;

	private List<Long> perticipants;
	private List<Long> documents;
	private List<CreateEventChecklistReqDto> checklists;

	public Event getBean() {
		return Event.builder()
				.title(title)
				.description(description)
				.projectId(projectId)
				.categoryId(categoryId)
				.eventDate(eventDate)
				.startTime(startTime == null ? LocalTime.MIDNIGHT : startTime)
				.endTime(endTime == null ? LocalTime.of(23, 59) : endTime)
				.location(location)
				.isReminderEnabled(reminderBefore != null)
				.reminderBefore(reminderBefore)
				.eventLink(eventLink)
				.build();
	}
}