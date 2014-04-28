package br.datamaio.fly.quartz.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Anotação de Validação da CronException
 *
 * @author Fernando Rubbo
 */
@Documented
@Constraint(validatedBy = CronExpressionValidator.class)
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface Cron {
	/** Mensagem padrão que será apresentada para o usuário */
	String message() default "A expressão Cron é inválida!";
    /** Grupos de validação */
	Class<?>[] groups() default { };
	/** */
    Class<? extends Payload>[] payload() default { };
}
