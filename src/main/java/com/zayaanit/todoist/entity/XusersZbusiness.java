package com.zayaanit.todoist.entity;

import com.zayaanit.todoist.entity.pk.XusersZbusinessPK;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Zubayer Ahamed
 * @since Jun 22, 2025
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(XusersZbusinessPK.class)
@Table(name = "xusers_zbusiness")
@EqualsAndHashCode(callSuper = true)
public class XusersZbusiness extends AbstractModel<Long> {

	private static final long serialVersionUID = 8172997495726591542L;

	@Id
	@Column(name = "zuser")
	private Long zuser;

	@Id
	@Column(name = "zid")
	private Long zid;

	@Column(name = "zprimary", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean zprimary;

	@Column(name = "zadmin", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean zadmin;

	@Column(name = "zcollaborator", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean zcollaborator;

}
