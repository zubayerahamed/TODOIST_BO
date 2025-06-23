package com.zayaanit.todoist.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

/**
 * Zubayer Ahamed
 * @since Jun 19, 2025
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "zbusiness")
@EqualsAndHashCode(callSuper = true)
public class Zbusiness extends AbstractModel<Long> {

	private static final long serialVersionUID = 2932605023333073712L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "zid")
	private Long zid;

	@Column(name = "zorg", length = 100)
	private String zorg;

	@Column(name = "zactive", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean zactive;

	@Lob
	@Column(name = "xlogo")
	private byte[] xlogo;
}
