package br.com.chai.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public final class NumberUtil {
    public static BigDecimal parse(final String str) {
        BigDecimal bigDecimal = null;

        if (str != null) {
            try {
                DecimalFormat df = getDecimalFormatter();
                Number number = df.parse(str);
                if (number.longValue() == number.doubleValue()) {
                    bigDecimal = BigDecimal.valueOf(number.longValue());
                } else {
                    bigDecimal = BigDecimal.valueOf(number.doubleValue());
                }
            } catch (Exception e) {
                throw new RuntimeException("Não foi possível converver o texto '" + str + "' para valor numérico. Motivo: " + e.getMessage(), e);
            }

        }

        return bigDecimal;
    }

    /**
     * Define o DEFAULT_LOCALE a ser utilizado pelo SIZ
     */
    public static final Locale DEFAULT_LOCALE = new Locale("pt", "BR");


    /**
     * Formata um número de acordo com o Locale. Usado para números inteiros
     *
     * @param value
     *            Valor
     * @return Número formatado
     */
    public static String format(final Number value) {
        return format(value, 0, null);
    }

    /**
     * Formata um número de acordo com o Locale
     *
     * @param value
     *            Valor
     * @param scale
     *            Número de caracteres decimal
     * @return Número formatado
     */
    public static String format(final Number value, final Integer scale) {
        return format(value, scale, null);
    }

    /**
     * Formata um número de acordo com o Locale e agrupando
     *
     * @param value
     *            Valor
     * @param scale
     *            Número de caracteres decimal
     * @param simboloMoeda
     *            Símbolo do valor monetário
     * @return Número formatado
     */
    public static String format(final Number value, final Integer scale, final String simboloMoeda) {
        return format(value, scale, simboloMoeda, true);
    }

    /**
     * Formata um número de acordo com o Locale default
     *
     * @param value
     *            Valor
     * @param scale
     *            Número de caracteres decimal
     * @param simboloMoeda
     *            Símbolo do valor monetário
     * @param groupingUsed
     *            Indica se deve agrupar os valores
     * @return Número formatado
     */
    public static String format(final Number value, final Integer scale, final String simboloMoeda, final boolean groupingUsed) {
        return format(value, scale, simboloMoeda, groupingUsed, DEFAULT_LOCALE);
    }

    /**
     * Formata um número de acordo com o Locale default
     *
     * @param value
     * @param scale
     * @param simboloMoeda
     * @param groupingUsed
     * @param locale
     * @return
     */
    public static String format(final Number value, final Integer scale, final String simboloMoeda, final boolean groupingUsed, final Locale locale) {
        if (value == null) {
            return null;
        }

        BigDecimal valor = new BigDecimal(value.doubleValue(), MathContext.DECIMAL128);
        valor = valor.setScale(scale, RoundingMode.HALF_EVEN);
        DecimalFormat df = getDecimalFormatter(groupingUsed, locale);
        df.setMinimumFractionDigits(scale);
        String valorString = df.format(valor);

        if (!StringUtil.isBlank(simboloMoeda)) {
            return simboloMoeda + " " + valorString;
        }

        return valorString;
    }

    /**
     * Retorna uma instância de DecimalFormat para formatar a exibição de uma campo de aconrdo com o adapter
     *
     * @return DecimalFormat
     */
    public static DecimalFormat getDecimalFormatter() {
        return getDecimalFormatter(true);
    }

    /**
     * Retorna uma instância de DecimalFormat para formatar a exibição de uma campo de aconrdo com o adapter
     *
     * @param groupingUsed
     * @return
     */
    public static DecimalFormat getDecimalFormatter(final boolean groupingUsed) {
        return getDecimalFormatter(groupingUsed, DEFAULT_LOCALE);
    }

    /**
     * Retorna uma instância de DecimalFormat para formatar a exibição de uma campo de aconrdo com o adapter
     *
     * @param groupingUsed
     * @return
     */
    public static DecimalFormat getDecimalFormatter(final boolean groupingUsed, final Locale locale) {
        DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(locale);
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(locale);
        df.setDecimalFormatSymbols(dfs);
        df.setGroupingUsed(groupingUsed);
        df.setMinimumIntegerDigits(1);
        return df;
    }
}
