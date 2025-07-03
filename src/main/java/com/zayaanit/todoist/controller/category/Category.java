package com.zayaanit.todoist.controller.category;

import com.zayaanit.todoist.model.AbstractModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Zubayer Ahamed
 * @since Jun 19, 2025
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categories")
@EqualsAndHashCode(callSuper = true)
public class Category extends AbstractModel<Long> {

	private static final long serialVersionUID = 2932605023333073712L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "reference_id", nullable = false)
	private Long referenceId;

	@Column(name = "name", length = 25, nullable = false)
	private String name;

	@Column(name = "color", length = 10)
	private String color;

	@Column(name = "is_for_task", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean isForTask;

	@Column(name = "is_for_event", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean isForEvent;

	@Column(name = "seqn")
	private Integer seqn;
}
