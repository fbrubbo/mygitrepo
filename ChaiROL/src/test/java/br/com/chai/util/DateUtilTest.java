package br.com.chai.util;

import static java.math.BigDecimal.ZERO;
import static java.util.Calendar.DAY_OF_MONTH;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import static br.com.chai.util.DateUtil.getBegin;
import static br.com.chai.util.DateUtil.getEnd;
import static br.com.chai.util.DateUtil.monthDiff;

import java.math.BigDecimal;
import java.util.Calendar;

import org.junit.Test;

public class DateUtilTest {

    @Test(expected=NullPointerException.class)
    public void monthDiff_BeginIsNull(){
        Calendar begin = null;
        Calendar end = getEnd(28, 11, 2013);
        monthDiff(begin, end);
    }

    @Test(expected=NullPointerException.class)
    public void monthDiff_EndIsNull(){
        Calendar begin = getBegin(28, 11, 2013);
        Calendar end = null;
        monthDiff(begin, end);
    }

    @Test(expected=IllegalArgumentException.class)
    public void monthDiff_BeginBiggerThanEnd(){
        Calendar begin = getBegin(29, 11, 2013);
        Calendar end = getEnd(28, 11, 2013);
        monthDiff(begin, end);
    }

    @Test
    public void monthDiff_Equals(){
        Calendar begin = getBegin(28, 11, 2013);
        Calendar end = getBegin(28, 11, 2013);
        BigDecimal months = monthDiff(begin, end);
        assertThat(months, is(ZERO));
    }

    @Test
    public void monthDiff_DateEqualTimeDifferent(){
        Calendar begin = getBegin(28, 11, 2013);
        Calendar end = getEnd(28, 11, 2013);
        BigDecimal months = monthDiff(begin, end);
        assertThat(months, is(new BigDecimal("0.04")));
    }


    @Test
    public void dayDiff_TwoDay(){
        Calendar begin = getBegin(21, 11, 2013);
        Calendar end = (Calendar)begin.clone();
        end.add(DAY_OF_MONTH, 1);
        int days = DateUtil.dayDiff(begin, end);
        assertThat(days, is(2));
    }

    @Test
    public void monthDiff_OneDay(){
        Calendar begin = getBegin(21, 11, 2013);
        Calendar end = (Calendar)begin.clone();
        end.add(DAY_OF_MONTH, 1);
        BigDecimal months = monthDiff(begin, end);
        assertThat(months, is(new BigDecimal("0.07")));
    }
//
//    @Test
//    public void dayDiff_FifteenDays(){
//        Calendar begin = get(21, 11, 2013);
//        Calendar end = (Calendar)begin.clone();
//        end.add(DAY_OF_MONTH, 15);
//        int days = dayDiff(begin, end);
//        assertThat(days, is(16)); // pois contabiliza o dia também
//    }
//
//    @Test
//    public void monthDiff_FifteenDays(){
//        Calendar begin = get(21, 11, 2013);
//        Calendar end = (Calendar)begin.clone();
//        end.add(DAY_OF_MONTH, 15);
//        BigDecimal months = monthDiff(begin, end);
//        assertThat(months, is(new BigDecimal("0.52")));
//    }
//
//    @Test
//    public void dayDiff_ThirtyDays(){
//        Calendar begin = get(21, 11, 2013);
//        Calendar end = (Calendar)begin.clone();
//        end.add(MONTH, 1);
//        end.add(DAY_OF_MONTH, -1);
//        int days = dayDiff(begin, end);
//        assertThat(days, is(30));
//    }
//
//    @Test
//    public void monthDiff_ThirtyDays(){
//        Calendar begin = get(21, 11, 2013);
//        Calendar end = (Calendar)begin.clone();
//        end.add(MONTH, 1);
//        end.add(DAY_OF_MONTH, -1);
//        BigDecimal months = monthDiff(begin, end);
//        assertThat(months, is(new BigDecimal("0.97")));
//    }
//    @Test
//    public void monthDiff_ExactlyOneMonth(){
//        Calendar begin = get(21, 11, 2013);
//        Calendar end = get(21, 12, 2013);
//        BigDecimal months = monthDiff(begin, end);
//        assertThat(months, is(new BigDecimal("1.00")));
//    }
//
//    @Test
//    public void monthDiff_OneMonthAndAHalf(){
//        Calendar begin = get(1, 11, 2013);
//        Calendar end = get(16, 12, 2013);
//        BigDecimal months = monthDiff(begin, end);
//        assertThat(months, is(new BigDecimal("1.50")));
//    }
//
//    // -------------
//
//    @Test
//    public void monthDiffInYear_BeginBiggerThanYear(){
//        Calendar begin = get(1, 11, 2013);
//        Calendar end = get(16, 12, 2013);
//        BigDecimal months = monthDiffInYear(begin, end, 2012);
//        assertThat(months, is(BigDecimal.ZERO));
//    }
//
//    @Test
//    public void monthDiffInYear_EndLesserThanYear(){
//        Calendar begin = get(1, 11, 2013);
//        Calendar end = get(16, 12, 2013);
//        BigDecimal months = monthDiffInYear(begin, end, 2014);
//        assertThat(months, is(BigDecimal.ZERO));
//    }
//
//    @Test
//    public void monthDiffInYear_TreeMonth(){
//        Calendar begin = get(1, 10, 2013);
//        Calendar end = get(16, 3, 2014);
//        BigDecimal months = monthDiffInYear(begin, end, 2013);
//        assertThat(months, is(new BigDecimal("3.00")));
//    }
//
//    @Test
//    public void monthDiffInYear_TwoMonthAndHalf(){
//        Calendar begin = get(1, 10, 2013);
//        Calendar end = get(16, 3, 2014);
//        BigDecimal months = monthDiffInYear(begin, end, 2014);
//        assertThat(months, is(new BigDecimal("2.50")));
//    }
//
//    @Test
//    public void monthDiffInYear_TwelveMonths(){
//        Calendar begin = get(1, 10, 2013);
//        Calendar end = get(16, 3, 2015);
//        BigDecimal months = monthDiffInYear(begin, end, 2014);
//        assertThat(months, is(new BigDecimal("12.00")));
//    }
//
//    // -------------
//
//    @Test
//    public void monthDiffInYear_app(){
//        Calendar begin = get(1, 10, 2013);
//        Calendar end = get(16, 3, 2015);
//        BigDecimal total = monthDiff(begin, end);
//        assertThat(total, is(new BigDecimal("17.50")));
//
//        BigDecimal months = monthDiffInYear(begin, end, 2013);
//        assertThat(months, is(new BigDecimal("3.00")));
//
//        months = monthDiffInYear(begin, end, 2014);
//        assertThat(months, is(new BigDecimal("12.00")));
//
//        months = monthDiffInYear(begin, end, 2015);
//        assertThat(months, is(new BigDecimal("2.50")));
//    }
//
//    @Test
//    public void validDates(){
//        Calendar actual = DateUtil.parse("31.12.2013");
//        Calendar expected = DateUtil.get(31, 12, 2013);
//        assertThat(actual, is(expected));
//    }
//
//    @Test(expected=Exception.class)
//    public void invalidDates(){
//        DateUtil.parse("32.12.2013");
//    }
//
//    @Test(expected=Exception.class)
//    public void invalidDates2(){
//        DateUtil.parse("31.12.9999");
//    }
}
