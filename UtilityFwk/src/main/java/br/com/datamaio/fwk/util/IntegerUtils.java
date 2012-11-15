package br.com.datamaio.fwk.util;

import java.text.DecimalFormat;

public class IntegerUtils {
	
	public static String toText(Integer value, Integer size) {
		return StringUtils.leftPad(Integer.toString(value), '0', size);
	}

	public static Integer toInteger(String str) {
		str = str.trim();
		if (StringUtils.isEmpty(str) || !StringUtils.isNumeric(str)) {
			return null;
		} else {
			return Integer.parseInt(str);
		}
	}
	
	public static String format(final Integer value, final DecimalFormat df) {
		return value == null ? "" : df.format(value);
	}	
	
}
