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
                throw new RuntimeException("N�o foi poss�vel converver o texto '" + str + "' para valor num�rico. Motivo: " + e.getMessage(), e);
            }

        }

        return bigDecimal;
    }

    /**
     * Define o DEFAULT_LOCALE a ser utilizado pelo SIZ
     */
    public static final Locale DEFAULT_LOCALE = new Locale("pt", "BR");


    /**
     * Formata um n�mero de acordo com o Locale. Usado para n�meros inteiros
     *
     * @param value
     *            Valor
     * @return N�mero formatado
     */
    public static String format(final Number value) {
        return format(value, 0, null);
    }

    /**
     * Formata um n�mero de acordo com o Locale
     *
     * @param value
     *            Valor
     * @param scale
     *            N�mero de caracteres decimal
     * @return N�mero formatado
     */
    public static String format(final Number value, final Integer scale) {
        return format(value, scale, null);
    }

    /**
     * Formata um n�mero de acordo com o Locale e agrupando
     *
     * @param value
     *            Valor
     * @param scale
     *            N�mero de caracteres decimal
     * @param simboloMoeda
     *            S�mbolo do valor monet�rio
     * @return N�mero formatado
     */
    public static String format(final Number value, final Integer scale, final String simboloMoeda) {
        return format(value, scale, simboloMoeda, true);
    }

    /**
     * Formata um n�mero de acordo com o Locale default
     *
     * @param value
     *            Valor
     * @param scale
     *            N�mero de caracteres decimal
     * @param simboloMoeda
     *            S�mbolo do valor monet�rio
     * @param groupingUsed
     *            Indica se deve agrupar os valores
     * @return N�mero formatado
     */
    public static String format(final Number value, final Integer scale, final String simboloMoeda, final boolean groupingUsed) {
        return format(value, scale, simboloMoeda, groupingUsed, DEFAULT_LOCALE);
    }

    /**
     * Formata um n�mero de acordo com o Locale default
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
     * Retorna uma inst�ncia de DecimalFormat para formatar a exibi��o de uma campo de aconrdo com o adapter
     *
     * @return DecimalFormat
     */
    public static DecimalFormat getDecimalFormatter() {
        return getDecimalFormatter(true);
    }

    /**
     * Retorna uma inst�ncia de DecimalFormat para formatar a exibi��o de uma campo de aconrdo com o adapter
     *
     * @param groupingUsed
     * @return
     */
    public static DecimalFormat getDecimalFormatter(final boolean groupingUsed) {
        return getDecimalFormatter(groupingUsed, DEFAULT_LOCALE);
    }

    /**
     * Retorna uma inst�ncia de DecimalFormat para formatar a exibi��o de uma campo de aconrdo com o adapter
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
