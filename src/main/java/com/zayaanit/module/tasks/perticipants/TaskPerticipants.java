package com.zayaanit.module.tasks.perticipants;

import com.zayaanit.enums.PerticipantAccess;
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
@IdClass(TaskPerticipantsPK.class)
@Table(name = "tasks_perticipants")
@EqualsAndHashCode(callSuper = true)
public class TaskPerticipants extends AbstractModel<Long> {

	private static final long serialVersionUID = 2572842083641054917L;

	@Id
	@Column(name = "task_id")
	private Long taskId;

	@Id
	@Column(name = "user_id")
	private Long userId;

	@Enumerated(EnumType.STRING)
	@Column(name = "perticipant_type", nullable = false)
	private PerticipantType perticipantType;

	@Enumerated(EnumType.STRING)
	@Column(name = "perticipant_access", nullable = false)
	private PerticipantAccess perticipantAccess;
}