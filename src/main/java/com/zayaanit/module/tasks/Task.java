package com.zayaanit.module.tasks;

import java.time.LocalDate;

import com.zayaanit.enums.PriorityType;
import com.zayaanit.model.AbstractModel;

import jakarta.persistence.Column;
/**
 * Zubayer Ahamed
 * @since Jul 3, 2025
 */
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "tasks")
@EqualsAndHashCode(callSuper = true)
public class Task extends AbstractModel<Long> {

	private static final long serialVersionUID = 2572842083641054917L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "title", length = 100, nullable = false)
	private String title;

	@Lob
	@Column(name = "description")
	private String description;

	@Column(name = "project_id", nullable = false)
	private Long projectId;

	@Column(name = "section_id")
	private Long sectionId;

	@Column(name = "category_id")
	private Long categoryId;

	@Column(name = "workflow_id")
	private Long workflowId;



	@Column(name = "task_date", nullable = true)
	private LocalDate taskDate; // Target user email for notification

	@Column(name = "due_date", nullable = true)
	private LocalDate dueDate;

	@Column(name = "estTime", nullable = true)
	private Integer estTime; // In minute

	@Enumerated(EnumType.STRING)
	@Column(name = "priority", nullable = true)
	private PriorityType priority;

	@Column(name = "is_completed", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean isCompleted;
}