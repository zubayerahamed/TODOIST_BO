package com.zayaanit.module.events;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

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
@ValidUpdateEventRequest
public class UpdateEventReqDto {

	@NotNull(message = "Event id required")
	private Long id;

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

	private List<Long> perticipants;
	private List<Long> documents;
}