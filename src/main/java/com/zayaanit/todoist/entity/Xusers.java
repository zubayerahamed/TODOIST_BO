package com.zayaanit.todoist.entity;

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
 * 
 * @since Jun 17, 2025
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "xusers")
@EqualsAndHashCode(callSuper = true)
public class Xusers extends AbstractModel<Long> {

	private static final long serialVersionUID = -6481139188124990692L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "zuser")
	private Long zuser;

	@Column(name = "zemail", length = 255, unique = true, nullable = false)
	private String zemail;

	@Column(name = "xpassword", length = 100, nullable = false)
	private String xpassword;

	@Column(name = "xfname", length = 50)
	private String xfname;

	@Column(name = "xlname", length = 50)
	private String xlname;

	@Column(name = "zactive", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean zactive;
}
