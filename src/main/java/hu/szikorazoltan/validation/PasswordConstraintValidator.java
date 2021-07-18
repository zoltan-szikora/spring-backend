package hu.szikorazoltan.validation;

import java.util.Arrays;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.EnglishSequenceData;
import org.passay.IllegalSequenceRule;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

	@Override
	public void initialize(ValidPassword arg0) {
	}

	@Override
	public boolean isValid(String password, ConstraintValidatorContext context) {
		PasswordValidator validator = new PasswordValidator(Arrays.asList(

			// length between 6 and 20 characters
			new LengthRule(6, 20),
	
			// at least one upper-case character
			new CharacterRule(EnglishCharacterData.UpperCase, 1),
	
			// at least one digit character
			new CharacterRule(EnglishCharacterData.Digit, 1),
	
			// at least one symbol (special character)
			// new CharacterRule(EnglishCharacterData.Special, 1),
	
			// define some illegal sequences that will fail when >= 3 chars long
			// alphabetical is of the form 'abcde', numerical is '34567', qwery is 'asdfg'
			// the false parameter indicates that wrapped sequences are allowed; e.g.
			// 'xyzabc'
			new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 3, false),
			new IllegalSequenceRule(EnglishSequenceData.Numerical, 3, false),
			new IllegalSequenceRule(EnglishSequenceData.USQwerty, 3, false),
	
			// no whitespace
			new WhitespaceRule()));

		RuleResult result = validator.validate(new PasswordData(password));
		if (result.isValid()) {
			return true;
		}
		return false;
	}
}
