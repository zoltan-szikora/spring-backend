package hu.szikorazoltan.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import hu.szikorazoltan.model.User;
import hu.szikorazoltan.service.UserService;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

	@Autowired
	private UserService userService;

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		User userCheckEmail = userService.findByEmail(value);
		return (userCheckEmail == null) ? true : false;
	}

}
