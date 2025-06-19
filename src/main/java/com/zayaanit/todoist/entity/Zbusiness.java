package com.zayaanit.todoist.entity;

import java.util.Base64;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Zubayer Ahamed
 * @since Jun 19, 2025
 */
@Data
@Entity
@Table(name = "zbusiness")
@EqualsAndHashCode(callSuper = true)
public class Zbusiness extends AbstractModel<String> {

	private static final long serialVersionUID = 2932605023333073712L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Column(name = "zorg", length = 100)
	private String zorg;

	@Column(name = "zactive")
	private Boolean zactive;

	@Lob
	@Column(name = "xlogo")
	private byte[] xlogo;

	@Transient
	private String imageBase64;

	public String getImageBase64() {
		if(this.xlogo == null || this.xlogo.length <= 0) return "";
		return Base64.getEncoder().encodeToString(this.xlogo);
	}
}
