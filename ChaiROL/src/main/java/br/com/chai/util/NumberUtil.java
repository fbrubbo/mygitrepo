package br.com.chai.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public final class NumberUtil {
    public static BigDecimal parse(final String str) {
        BigDecimal bigDecimal = null;

        if (str != null) {
            try {
                NumberFormat NF = new DecimalFormat ("#,##0.00", new DecimalFormatSymbols (new Locale ("pt", "BR")));
                Number number = NF.parse(str);
                if (number.longValue() == number.doubleValue()) {
                    bigDecimal = BigDecimal.valueOf(number.longValue());
                } else {
                    bigDecimal = BigDecimal.valueOf(number.doubleValue());
                }
            } catch (Exception e) {
                throw new RuntimeException("Não foi possível converver o texto " + str + " para valor numérico. Causa: " + e.getMessage());
            }

        }

        return bigDecimal;
    }
}
