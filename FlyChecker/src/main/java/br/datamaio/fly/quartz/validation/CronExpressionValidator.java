package br.datamaio.fly.quartz.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.quartz.CronExpression;

/**
 * Validador da Anotação de CronException (veja {@link Cron})
 *
 * ver: http://docs.oracle.com/cd/E12058_01/doc/doc.1014/e12030/cron_expressions.htm
 *
 * @author Fernando Rubbo
 */
public class CronExpressionValidator implements ConstraintValidator<Cron, String> {

	@Override
	public void initialize(final Cron ann) { }

	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext context) {
		if (value != null) {
			return CronExpression.isValidExpression(value);
		}
		return true;
	}
}
