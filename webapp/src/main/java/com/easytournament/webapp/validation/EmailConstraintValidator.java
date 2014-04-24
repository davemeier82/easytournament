package com.easytournament.webapp.validation;

import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * ConstraintValidator for Email Input
 */
public class EmailConstraintValidator implements
		ConstraintValidator<Email, String> {

	private Pattern pattern;

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	@Override
	public void initialize(Email a) {
		pattern = Pattern.compile(EMAIL_PATTERN);
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext cvc) {
		if (value == null) {
			return true;
		}

		return pattern.matcher(value.toString()).matches();
	}

}