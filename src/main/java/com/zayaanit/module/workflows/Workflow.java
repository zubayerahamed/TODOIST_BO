package com.zayaanit.module.workflows;

import com.zayaanit.model.AbstractModel;

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
 * @since Jul 2, 2025
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "workflow")
@EqualsAndHashCode(callSuper = true)
public class Workflow extends AbstractModel<Long> {

	private static final long serialVersionUID = 3297873396881753994L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "reference_id")
	private Long referenceId;   // It can be workspace id or can be project id

	@Column(name = "name", length = 25)
	private String name;

	@Column(name = "is_system_defined", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean isSystemDefined;

	@Column(name = "seqn")
	private Integer seqn;

	@Column(name = "color", length = 10)
	private String color;
}
