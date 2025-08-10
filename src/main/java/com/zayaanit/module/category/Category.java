package com.zayaanit.module.category;

import com.zayaanit.enums.ReferenceType;
import com.zayaanit.model.AbstractModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

	private static final long serialVersionUID = -9016326056400831749L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "reference_id", nullable = false)
	private Long referenceId;

	@Enumerated(EnumType.STRING)
	@Column(name = "reference_type", nullable = false)
	private ReferenceType referenceType;

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

	@Column(name = "is_default_for_task", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean isDefaultForTask;

	@Column(name = "is_default_for_event", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean isDefaultForEvent;
}
