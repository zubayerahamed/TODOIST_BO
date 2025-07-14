package com.zayaanit.module.events.perticipants;

import com.zayaanit.enums.PerticipantType;
import com.zayaanit.model.AbstractModel;

import jakarta.persistence.Column;
/**
 * Zubayer Ahamed
 * @since Jul 3, 2025
 */
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
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
@IdClass(EventPerticipantsPK.class)
@Table(name = "events_perticipants")
@EqualsAndHashCode(callSuper = true)
public class EventPerticipants extends AbstractModel<Long> {

	private static final long serialVersionUID = 2572842083641054917L;

	@Id
	@Column(name = "event_id")
	private Long eventId;

	@Id
	@Column(name = "user_id")
	private Long userId;

	@Enumerated(EnumType.STRING)
	@Column(name = "perticipant_type", nullable = false)
	private PerticipantType perticipantType;

	@Column(name = "is_reminder_sent", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean isReminderSent;
}