package br.com.datamaio.fwk.criteria.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Restriction {
	String prop() default "";
	
	//FIXME: No Java8 trocar isto por clousure. ai eu posso passar qualquer tipo NSCriteria ou Criteria do hibernate e é type safe
	// apesar que no método addRestrictions() do SearchableDao eu já faco a verificação do  
	String criterion() default "eq";
}
