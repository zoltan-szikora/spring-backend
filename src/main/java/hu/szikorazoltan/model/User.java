package hu.szikorazoltan.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;

import hu.szikorazoltan.validation.ValidDate;
import hu.szikorazoltan.validation.ValidEmail;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "id")
	private long id;

	@Column(name = "first_name", length = 50)
	@Pattern(regexp = "^[A-Za-záéiíoóöőuúüűÁÉIÍOÓÖŐUÚÜŰ]{2,20}", message = "{user.firstName.pattern}")
	@NotBlank(message = "{user.firstName.NotBlank}")
	private String firstName;

	@Column(name = "last_name", length = 50)
	@Pattern(regexp = "^[A-Za-záéiíoóöőuúüűÁÉIÍOÓÖŐUÚÜŰ]{2,20}", message = "{user.lastName.pattern}")
	@NotBlank(message = "{user.lastName.NotBlank}")
	private String lastName;

	@Column(name = "date_of_birth")
	@Past(message = "{user.dateOfBirth.Past}")
	@NotNull(message = "{user.dateOfBirth.NotNull}")
	@ValidDate(message = "{user.dateOfBirth.NotValid}")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfBirth;

	@Column(name = "username", unique = true, nullable = false, length = 50)
	@NotBlank(message = "{user.username.NotBlank}")
	private String username;

	@Column(name = "email", unique = true, nullable = false)
	@NotBlank(message = "{user.email.NotBlank}")
	@ValidEmail(message = "{user.email.NotValid}")
	private String email;

	@Column(name = "password", nullable = false)
	@NotBlank(message = "{user.password.NotBlank}")
	private String password;

	@Column(name = "activation")
	private String activation;

	@Column(name = "enabled")
	private Boolean enabled;

	@Column(name = "status", nullable = false, length = 1)
	@Enumerated(EnumType.STRING)
	private Status status;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "users_roles", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
			@JoinColumn(name = "role_id") })
	private Set<Role> roles = new HashSet<Role>();

	public void addRoles(String roleName) {
		if (this.roles == null || this.roles.isEmpty()) {
			this.roles = new HashSet<>();
		}
		this.roles.add(new Role(roleName));
	}
}
