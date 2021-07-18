package hu.szikorazoltan.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import hu.szikorazoltan.model.User;
import hu.szikorazoltan.service.UserService;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

	@Autowired
	private UserService userService;

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		User userCheckUsername = userService.findByUsername(value);
		return (userCheckUsername == null) ? true : false;
	}
}
