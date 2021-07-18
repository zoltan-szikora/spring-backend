package hu.szikorazoltan.validation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DateValidator implements ConstraintValidator<ValidDate, LocalDate> {

	private static final Pattern DATE_PATTERN = Pattern
			.compile("^((2000|2400|2800|(19|2[0-9](0[48]|[2468][048]|[13579][26])))-02-29)$"
					+ "|^(((19|2[0-9])[0-9]{2})-02-(0[1-9]|1[0-9]|2[0-8]))$"
					+ "|^(((19|2[0-9])[0-9]{2})-(0[13578]|10|12)-(0[1-9]|[12][0-9]|3[01]))$"
					+ "|^(((19|2[0-9])[0-9]{2})-(0[469]|11)-(0[1-9]|[12][0-9]|30))$");

	@Override
	public boolean isValid(final LocalDate date, ConstraintValidatorContext context) {
		if(date == null) return false;
		String dateToIsoString = date.format(DateTimeFormatter.ISO_DATE);
		return (dateMatches(dateToIsoString));
	}

	public boolean dateMatches(final String date) {
		Matcher matcher = DATE_PATTERN.matcher(date);
		return matcher.matches();
	}
}
