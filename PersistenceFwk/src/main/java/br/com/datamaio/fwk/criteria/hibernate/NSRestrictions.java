package br.com.datamaio.fwk.criteria.hibernate;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.NaturalIdentifier;
import org.hibernate.criterion.PropertyExpression;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.hibernate.type.Type;

import br.com.datamaio.fwk.util.StringUtils;

/**
 * Esta classe encapsula a lógica de <code>Null Safe</code> para o {@link Restrictions} do hibernate.<br>
 * Para mais detalhes, ver documentação do hibernate.
 * 
 * @author Fernando Rubbo
 */
public class NSRestrictions {

	public static final int X = 10;
	
	public static Criterion idEq(Object value)
	{
	  return isValueNull(value)? null : Restrictions.idEq(value);
	}

	public static SimpleExpression eq(String propertyName, Object value)
	{
	  return isValueNull(value)? null : Restrictions.eq(propertyName, value);
	}

	private static boolean isValueNull(Object value) {
		return value==null || 
			(value instanceof String && StringUtils.isEmpty((String)value));
	}

	public static SimpleExpression ne(String propertyName, Object value)
	{
	  return isValueNull(value)? null : Restrictions.ne(propertyName, value);
	}

	public static SimpleExpression like(String propertyName, Object value)
	{
	  return isValueNull(value)? null : Restrictions.like(propertyName, value.toString(), MatchMode.ANYWHERE);
	}

	public static SimpleExpression like(String propertyName, String value, MatchMode matchMode)
	{
	  return isValueNull(value)? null : Restrictions.like(propertyName, value, matchMode);
	}

	public static Criterion ilike(String propertyName, String value, MatchMode matchMode)
	{
	  return isValueNull(value)? null : Restrictions.ilike(propertyName, value, matchMode);
	}

	public static Criterion ilike(String propertyName, Object value)
	{
	  return isValueNull(value)? null : Restrictions.ilike(propertyName, value.toString(), MatchMode.ANYWHERE);
	}

	public static SimpleExpression gt(String propertyName, Object value)
	{
		return isValueNull(value)? null : Restrictions.gt(propertyName, value);
	}

	public static SimpleExpression lt(String propertyName, Object value)
	{
		return isValueNull(value)? null : Restrictions.lt(propertyName, value);
	}

	public static SimpleExpression le(String propertyName, Object value)
	{
		return isValueNull(value)? null : Restrictions.le(propertyName, value);
	}

	public static SimpleExpression ge(String propertyName, Object value)
	{
		return isValueNull(value)? null : Restrictions.ge(propertyName, value);
	}

	public static Criterion between(String propertyName, Object lo, Object hi)
	{
		return (isValueNull(lo) || isValueNull(hi))? null : Restrictions.between(propertyName, lo, hi);
	}

	public static Criterion in(String propertyName, Object[] values)
	{
		return (values==null || values.length==0)? null : Restrictions.in(propertyName, values);
	}

	public static Criterion in(String propertyName, Collection<?> values)
	{
		return (values==null || values.size()==0)? null : Restrictions.in(propertyName, values);
	}

	public static Criterion isNull(String propertyName)
	{
		return Restrictions.isNull(propertyName);		
	}

	public static PropertyExpression eqProperty(String propertyName, String otherPropertyName)
	{
		return Restrictions.eqProperty(propertyName, otherPropertyName);
	}

	public static PropertyExpression neProperty(String propertyName, String otherPropertyName)
	{
		return Restrictions.neProperty(propertyName, otherPropertyName);
	}

	public static PropertyExpression ltProperty(String propertyName, String otherPropertyName)
	{
		return Restrictions.ltProperty(propertyName, otherPropertyName);
	}

	public static PropertyExpression leProperty(String propertyName, String otherPropertyName)
	{
		return Restrictions.leProperty(propertyName, otherPropertyName);
	}

	public static PropertyExpression gtProperty(String propertyName, String otherPropertyName)
	{
		return Restrictions.gtProperty(propertyName, otherPropertyName);
	}

	public static PropertyExpression geProperty(String propertyName, String otherPropertyName)
	{
		return Restrictions.geProperty(propertyName, otherPropertyName);
	}

	public static Criterion isNotNull(String propertyName)
	{
		return Restrictions.isNotNull(propertyName);
	}

	public static LogicalExpression and(Criterion lhs, Criterion rhs)
	{
		return lhs==null || rhs==null? null : Restrictions.and(lhs, rhs);
	}

	public static LogicalExpression or(Criterion lhs, Criterion rhs)
	{
		return lhs==null || rhs==null? null : Restrictions.or(lhs, rhs);
	}

	public static Criterion not(Criterion expression)
	{
		return expression==null? null : Restrictions.not(expression);
	}

	public static Criterion sqlRestriction(String sql, Object[] values, Type[] types)
	{
		return values==null || values.length==0? null : Restrictions.sqlRestriction(sql, values, types);
	}

	public static Criterion sqlRestriction(String sql, Object value, Type type)
	{
		return isValueNull(value)? null : Restrictions.sqlRestriction(sql, value, type);
	}

	public static Criterion sqlRestriction(String sql)
	{
		return Restrictions.sqlRestriction(sql);
	}

	public static Conjunction conjunction()
	{
	  return new NSConjunction();
	}

	public static Disjunction disjunction()
	{
	  return new NSDisjunction();
	}

	public static Criterion allEq(Map<String, ? extends Object> propertyNameValues) {
		if (propertyNameValues == null)
			return null;

		Conjunction conj = conjunction();
		Set<String> keySet = propertyNameValues.keySet();
		for (String key : keySet) {
			Object value = propertyNameValues.get(key);
			conj.add(eq(key, value));
		}
		
		return conj;
	}

	public static Criterion isEmpty(String propertyName)
	{
	  return Restrictions.isEmpty(propertyName);
	}

	public static Criterion isNotEmpty(String propertyName)
	{
	  return Restrictions.isNotEmpty(propertyName);
	}

	public static Criterion sizeEq(String propertyName, int size)
	{
	  return Restrictions.sizeEq(propertyName, size);
	}

	public static Criterion sizeNe(String propertyName, int size)
	{
	  return Restrictions.sizeNe(propertyName, size);
	}

	public static Criterion sizeGt(String propertyName, int size)
	{
	  return Restrictions.sizeGt(propertyName, size);
	}

	public static Criterion sizeLt(String propertyName, int size)
	{
	  return Restrictions.sizeLt(propertyName, size);
	}

	public static Criterion sizeGe(String propertyName, int size)
	{
	  return Restrictions.sizeGe(propertyName, size);
	}

	public static Criterion sizeLe(String propertyName, int size)
	{
	  return Restrictions.sizeLe(propertyName, size);
	}

	public static NaturalIdentifier naturalId() {
	  return Restrictions.naturalId();
	}
}
