package com.zayaanit.todoist.controller.projects;

import com.zayaanit.todoist.enums.LayoutType;
import com.zayaanit.todoist.model.AbstractModel;

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
@Table(name = "projects")
@EqualsAndHashCode(callSuper = true)
public class Project extends AbstractModel<Long> {

	private static final long serialVersionUID = 2932605023333073712L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "workspace_id")
	private Long workspaceId;

	@Column(name = "name", length = 25)
	private String name;

	@Column(name = "color", length = 10)
	private String color;

	@Column(name = "is_favourite", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean isFavourite;

	@Column(name = "seqn")
	private Integer seqn;

	@Enumerated(EnumType.STRING)
	@Column(name = "layout_type")
	private LayoutType layoutType;

	@Column(name = "is_system_defined", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean isSystemDefined;
}
