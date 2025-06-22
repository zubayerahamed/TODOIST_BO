package com.zayaanit.todoist.entity;

import com.zayaanit.todoist.entity.pk.XusersPK;

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
 * @since Jun 17, 2025
 */
@Data
@Entity
@IdClass(XusersPK.class)
@Table(name = "xusers")
@EqualsAndHashCode(callSuper = true)
public class Xusers extends AbstractModel<String> {

	private static final long serialVersionUID = -6481139188124990692L;

	@Id
	@Basic(optional = false)
	@Column(name = "zuser")
	private Integer zuser;

	@Id
	@Basic(optional = false)
	@Column(name = "zemail", length = 255, unique = true)
	private String zemail;

	@Column(name = "xpassword", length = 255)
	private String xpassword;

	@Column(name = "zactive", length = 1)
	private Boolean zactive = Boolean.TRUE;
}
