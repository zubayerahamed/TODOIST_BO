package com.zayaanit.module.documents;

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
@Table(name = "documents")
@EqualsAndHashCode(callSuper = true)
public class Document extends AbstractModel<Long> {

	private static final long serialVersionUID = 2572842083641054917L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "reference_id")
	private Long referenceId;

	@Column(name = "title", length = 100)
	private String title;

	@Column(name = "description", length = 100)
	private String description;

	@Column(name = "doc_name", nullable = false, length = 100)
	private String docName;

	@Column(name = "old_name", nullable = false, length = 100)
	private String oldName;

	@Column(name = "doc_ext", length = 10)
	private String docExt;

	@Column(name = "doc_size", length = 20)
	private String docSize;

	@Column(name = "doc_type", length = 20)
	private String docType;

	private boolean xtemp;
}