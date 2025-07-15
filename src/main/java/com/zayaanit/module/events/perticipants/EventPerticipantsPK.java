package com.zayaanit.module.events.perticipants;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Zubayer Ahamed
 * @since Jul 13, 2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventPerticipantsPK implements Serializable {

	private static final long serialVersionUID = 7998317510591027680L;

	private Long eventId;
	private Long userId;
}
