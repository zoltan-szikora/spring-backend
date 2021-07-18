package hu.szikorazoltan.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.springframework.format.annotation.DateTimeFormat;

import hu.szikorazoltan.model.User;
import hu.szikorazoltan.validation.DateFormatHandler;
import hu.szikorazoltan.validation.FieldMatch;
import hu.szikorazoltan.validation.UniqueEmail;
import hu.szikorazoltan.validation.UniqueUsername;
import hu.szikorazoltan.validation.ValidDate;
import hu.szikorazoltan.validation.ValidEmail;
import hu.szikorazoltan.validation.ValidPassword;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@FieldMatch(first = "password", second = "confirmPassword", message = "{user.password.FieldMatch}")
public class UserRegistrationDto {

	@Pattern(regexp = "^[A-Za-záéiíoóöőuúüűÁÉIÍOÓÖŐUÚÜŰ]{2,20}", message = "{user.firstName.pattern}")
	@NotBlank(message = "{user.firstName.NotBlank}")
	private String firstName;

	@Pattern(regexp = "^[A-Za-záéiíoóöőuúüűÁÉIÍOÓÖŐUÚÜŰ]{2,20}", message = "{user.lastName.pattern}")
	@NotBlank(message = "{user.lastName.NotBlank}")
	private String lastName;

	@Past(message = "{user.dateOfBirth.Past}")
	@NotNull(message = "{user.dateOfBirth.NotNull}")
	@ValidDate(message = "{user.dateOfBirth.NotValid}")
	@JsonDeserialize(using = DateFormatHandler.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfBirth;

	@Pattern(regexp = "^[A-Za-z0-9_-]{3,16}", message = "{user.username.pattern}")
	@NotBlank(message = "{user.username.NotBlank}")
	@UniqueUsername(message = "{user.username.alreadyExist}")
	private String username;

	@ValidEmail(message = "{user.email.NotValid}")
	@UniqueEmail(message = "{user.email.alreadyExist}")
	private String email;

	@ValidPassword(message = "{user.password.NotValid}")
	private String password;

	@NotNull(message = "{user.confirmPassword.NotNull}")
	private String confirmPassword;

	public User convertToUser() {
		User user = new User();
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setDateOfBirth(dateOfBirth);
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(password);
		return user;
	}

}
