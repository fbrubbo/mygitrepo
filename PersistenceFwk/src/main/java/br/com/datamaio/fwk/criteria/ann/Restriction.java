package br.com.datamaio.fwk.criteria.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Restriction {
	String prop() default "";
	
	//FIXME: No Java8 trocar isto por clousure. ai eu posso passar qualquer tipo NSCriteria ou Criteria do hibernate e � type safe
	// apesar que no m�todo addRestrictions() do SearchableDao eu j� faco a verifica��o do  
	String criterion() default "eq";
}
