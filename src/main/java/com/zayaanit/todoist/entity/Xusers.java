package com.zayaanit.todoist.entity;

import com.zayaanit.todoist.entity.pk.XusersPK;
import com.zayaanit.todoist.enums.SubmitFor;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
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
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "zemail", length = 100)
	private String zemail;

	@Column(name = "xpassword", length = 25)
	private String xpassword;

	@Column(name = "xoldpassword", length = 25)
	private String xoldpassword;

	@Column(name = "zactive", length = 1)
	private Boolean zactive = Boolean.TRUE;

	@Transient
	private String businessName;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Xusers getDefaultInstance() {
		Xusers obj = new Xusers();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setZactive(Boolean.TRUE);
		return obj;
	}
}
