package com.zayaanit.module.events;

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
@Table(name = "events_checklists")
@EqualsAndHashCode(callSuper = true)
public class EventChecklist extends AbstractModel<Long> {

	private static final long serialVersionUID = 2572842083641054917L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "event_id", nullable = false)
	private Long eventId;

	@Lob
	@Column(name = "description", nullable = true)
	private String description;

	@Column(name = "is_completed", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean isCompleted;
}