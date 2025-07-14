package com.zayaanit.module.tasks.tags;

import com.zayaanit.model.AbstractModel;

import jakarta.persistence.Column;
/**
 * Zubayer Ahamed
 * @since Jul 3, 2025
 */
import jakarta.persistence.Entity;
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
@IdClass(TaskTagPK.class)
@Table(name = "tasks_tags")
@EqualsAndHashCode(callSuper = true)
public class TaskTag extends AbstractModel<Long> {

	private static final long serialVersionUID = 2572842083641054917L;

	@Id
	@Column(name = "task_id", nullable = false)
	private Long taskId;

	@Id
	@Column(name = "tag_id", nullable = false)
	private Long tagId;
}