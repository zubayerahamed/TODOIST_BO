package com.zayaanit.module.events;

import java.time.LocalDate;
import java.time.LocalTime;

import com.zayaanit.model.AbstractModel;

import jakarta.persistence.Column;
/**
 * Zubayer Ahamed
 * @since Jul 3, 2025
 */
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "parent_events")
@EqualsAndHashCode(callSuper = true)
public class ParentEvent extends AbstractModel<Long> {

	private static final long serialVersionUID = 2572842083641054917L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "title", length = 100, nullable = false)
	private String title;

	@Lob
	@Column(name = "description", nullable = true)
	private String description;

	@Column(name = "project_id", nullable = false)
	private Long projectId;

	@Column(name = "category_id")
	private Long categoryId;

	@Column(name = "event_date", nullable = false)
	private LocalDate eventDate;

	@Column(name = "start_time", nullable = false)
	private LocalTime startTime;

	@Column(name = "end_time", nullable = false)
	private LocalTime endTime;

	@Column(name = "location", length = 50)
	private String location;

	@Column(name = "event_link", length = 255)
	private String eventLink;

	@Column(name = "is_reminder_eanbled", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean isReminderEnabled;

	@Column(name = "reminder_before", nullable = false)
	private Integer reminderBefore; // How many minutes before to send reminder
}