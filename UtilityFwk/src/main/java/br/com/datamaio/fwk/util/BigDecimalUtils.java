package br.com.datamaio.fwk.util;

import static br.com.datamaio.fwk.util.StringUtils.isEmpty;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;

public class BigDecimalUtils {
	
	public static final String DEFAULT_MASK = "###,###,###,##0.00";
	public static final DecimalFormat DEFAULT_FORMAT = new DecimalFormat(DEFAULT_MASK);
	
	public static String format(final BigDecimal value) {
		return format(value, DEFAULT_FORMAT);
	}
	
	public static String format(final BigDecimal value, final DecimalFormat df) {
		return value == null ? "" : df.format(value);
	}	

	public static BigDecimal parse(String value){
		return parse(value, DEFAULT_FORMAT);
	}
	
	public static BigDecimal parse(String value, DecimalFormat df){		
		if (isEmpty(value)) {
			return null;
		}

		try {			
			DecimalFormat clonnedDF = (DecimalFormat) df.clone();
			clonnedDF.setParseBigDecimal(true);
			Number parse = clonnedDF.parse(value);
			return (BigDecimal)parse;
		} catch (Exception e) {
			throw new RuntimeException("Valor no formato incorreto: " + value, e);
		}
	}
	
    public static boolean isValid(String value) {
    	return isValid(value, false);
    }
    
    public static boolean isValid(String value, boolean allowNull) {
    	return isValid(value, allowNull, DEFAULT_FORMAT);
    }
    
	public static boolean isValid(String value, boolean allowNull, DecimalFormat df) {
		if (isEmpty(value)) {
			return allowNull;
		}

		try {			
			df.parse(value);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}
	
	public static BigDecimal getValue(Object value) {
		return value == null ? BigDecimal.ZERO : (BigDecimal) value;
	}
	
	public static String toText(BigDecimal value, Integer size) {
		return StringUtils.leftPad(format(value).replace(",", "").replace(".", ""), '0', size);
	}
	
	public static BigDecimal parseText(String text) {
		return new BigDecimal(text).divide(new BigDecimal("100"));
	}
	
	public static BigDecimal toPercent(BigDecimal value) {
		return value.divide(new BigDecimal("100"), 2);
	} 
	
	public static BigDecimal toPercent(BigDecimal value, BigDecimal total) {
		return value.multiply(new BigDecimal("100.00")).divide(total, 2);
	}
	
	public static String formatMonetary(final BigDecimal value) {
		return "R$ " + format(value, DEFAULT_FORMAT);
	}
	
}
