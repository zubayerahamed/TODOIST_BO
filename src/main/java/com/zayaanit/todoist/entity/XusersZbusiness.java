package com.zayaanit.todoist.entity;

import com.zayaanit.todoist.entity.pk.XusersZbusinessPK;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Zubayer Ahamed
 * @since Jun 22, 2025
 */
@Data
@Entity
@IdClass(XusersZbusinessPK.class)
@Table(name = "xusers_zbusiness")
@EqualsAndHashCode(callSuper = true)
public class XusersZbusiness extends AbstractModel<String> {

	private static final long serialVersionUID = 8172997495726591542L;

	@Id
	@Basic(optional = false)
	@Column(name = "zuser")
	private Integer zuser;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Column(name = "zprimary")
	private Boolean zprimary;

	@Column(name = "zadmin")
	private Boolean zadmin;

	@Column(name = "zcollaborator")
	private Boolean zcollaborator;

}
