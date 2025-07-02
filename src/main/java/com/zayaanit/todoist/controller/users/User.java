package com.zayaanit.todoist.controller.users;

import java.util.Date;

import com.zayaanit.todoist.model.AbstractModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
@Table(name = "users")
@EqualsAndHashCode(callSuper = true)
public class User extends AbstractModel<Long> {

	private static final long serialVersionUID = -6481139188124990692L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "email", length = 255, unique = true, nullable = false)
	private String email;

	@Column(name = "password", length = 100, nullable = false)
	private String password;

	@Column(name = "first_name", length = 50)
	private String firstName;

	@Column(name = "last_name", length = 50, nullable = true)
	private String lastName;

	@Column(name = "is_active", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean isActive;

	@Column(name = "country", length = 25, nullable = true)
	private String country;

	@Column(name = "phone", length = 25, nullable = true)
	private String phone;

	@Column(name = "location", length = 25, nullable = true)
	private String location;

	@Temporal(TemporalType.DATE)
	@Column(name = "date_of_birth", nullable = true)
	private Date dateOfBirth;
}
