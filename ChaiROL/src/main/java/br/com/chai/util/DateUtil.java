package br.com.chai.util;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.CEILING;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Objects.requireNonNull;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class DateUtil {
    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    public static Calendar parseBegin(final String str){
        try {
            DateFormat DF = new SimpleDateFormat("dd.MM.yyyy");
            DF.setLenient(false);
            Date d = DF.parse(str);
            Calendar c = toCalendarBegin(d);
            int year = c.get(Calendar.YEAR);
            if(year<1990 || year>2030){
                // isto foi colocado aqui porque existem datas loucas nas planilhas
                throw new RuntimeException("Não é possível entender a data :" + str);
            }
            return c;
        } catch (ParseException e) {
            throw new RuntimeException("Não é possível entender a data :" + str);
        }
    }

    public static Calendar parseEnd(final String str){
        try {
            DateFormat DF = new SimpleDateFormat("dd.MM.yyyy");
            DF.setLenient(false);
            Date d = DF.parse(str);
            Calendar c = toCalendarEnd(d);
            int year = c.get(Calendar.YEAR);
            if(year<1990 || year>2030){
                // isto foi colocado aqui porque existem datas loucas nas planilhas
                throw new RuntimeException("Não é possível entender a data :" + str);
            }
            return c;
        } catch (ParseException e) {
            throw new RuntimeException("Não é possível entender a data :" + str);
        }
    }


    public static Calendar firstDayOfYear(final int year) {
        return getBegin(1, 1, year);
    }

    public static Calendar lastDayOfYear(final int year) {
        return getEnd(31, 12, year);
    }

    public static Calendar getBegin(final int day, final int month, final int year) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.MONTH, month-1);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c;
    }

    public static Calendar getEnd(final int day, final int month, final int year) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.MONTH, month-1);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c;
    }

    public static Calendar toCalendarBegin(final Date d) {
        // seta as as 12hs para evitar problemas do horário de verão
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c;
    }

    public static Calendar toCalendarEnd(final Date d) {
        // seta as as 12hs para evitar problemas do horário de verão
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c;
    }


    public static BigDecimal monthDiffInYear(final Calendar begin, final Calendar end, final int year) {
        validateParameters(begin, end);
        Calendar beginInYear = null;
        Calendar endInYear = null;

        int beginYear = begin.get(Calendar.YEAR);
        if (beginYear > year) {
            return ZERO;
        } else if (beginYear < year) {
            beginInYear = firstDayOfYear(year);
        } else {
            beginInYear = (Calendar) begin.clone();
        }

        int endYear = end.get(Calendar.YEAR);
        if (endYear < year) {
            return ZERO;
        } else if (endYear > year) {
            endInYear = lastDayOfYear(year);
        } else {
            endInYear = (Calendar) end.clone();
        }

        return monthDiff(beginInYear, endInYear);
    }

    public static BigDecimal monthDiff(final Calendar begin, final Calendar end) {
        validateParameters(begin, end);

        if(begin.compareTo(end)==0){
            return ZERO;
        }

        BigDecimal months = ZERO;
        Calendar current = (Calendar) begin.clone();
        do {
            current.add(MONTH, 1);
            if(current.compareTo(end)<=0) {
                months = months.add(ONE);
            }
        }while(current.compareTo(end)<=0);

        // volta a ultima posição
        Calendar currentEnd = (Calendar)current.clone();
        current.add(MONTH, -1);
        Calendar currentBegin = (Calendar)current.clone();
        int numOfDaysToFitAMonth = dayDiff(currentBegin, currentEnd); //numOfDaysToFitAMonth        100%
        int actualNumOfDays = dayDiff(current, end);                  //actualNumOfDays             x

        BigDecimal x = new BigDecimal(actualNumOfDays).multiply(ONE_HUNDRED)
                                .divide(new BigDecimal(numOfDaysToFitAMonth), 10, CEILING);
        x = x.divide(ONE_HUNDRED, 2, CEILING);
        months = months.add(x);

        return months;
    }

    /**
     * Contabiliza o dia atual também
     */
    public static int dayDiff(final Calendar begin, final Calendar end) {
        validateParameters(begin, end);

        if(begin.compareTo(end)==0){
            return 0;
        }

        int days = 0;
        Calendar current = (Calendar) begin.clone();
        do{
            current.add(DAY_OF_MONTH, 1);
            if(current.compareTo(end)<=0) {
                days++;
            }
        }while(current.compareTo(end)<=0);

        Calendar endc = (Calendar)end.clone();
        endc.add(Calendar.MILLISECOND, 1);
        if(current.compareTo(endc)==0) {
            // Se falta apenas um milesegundo para fechar o dia, soma um dia
            days++;
        }

        return days;
    }


    private static void validateParameters(final Calendar begin, final Calendar end) {
        requireNonNull(begin);
        requireNonNull(end);
        if(begin.compareTo(end)>0) {
            throw new IllegalArgumentException("Data inicial não pode ser maior que data final!");
        }
    }
}
