package com.zayaanit.module.workspaces;

import com.zayaanit.model.AbstractModel;

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
@Table(name = "workspaces")
@EqualsAndHashCode(callSuper = true)
public class Workspace extends AbstractModel<Long> {

	private static final long serialVersionUID = 2932605023333073712L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "name", length = 100)
	private String name;

	@Column(name = "is_active", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean isActive;

	@Column(name = "is_system_defined", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean isSystemDefined;

	@Lob
	@Column(name = "logo", nullable = true)
	private byte[] logo;

	@Column(name = "is_weekend_sat", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean isWeekendSat;
	@Column(name = "is_weekend_sun", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean isWeekendSun;
	@Column(name = "is_weekend_mon", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean isWeekendMon;
	@Column(name = "is_weekend_tue", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean isWeekendTue;
	@Column(name = "is_weekend_wed", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean isWeekendWed;
	@Column(name = "is_weekend_thu", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean isWeekendThu;
	@Column(name = "is_weekend_fri", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean isWeekendFri;
}
