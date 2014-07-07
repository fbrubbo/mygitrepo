package br.datamaio.fly;

import static java.time.temporal.ChronoUnit.HOURS;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;


public final class SchedulerHelper {
    public static final TripOption OP1 = new TripOption(new BigDecimal("283.90"));
    public static final TripOption OP2 = new TripOption(new BigDecimal("343.90"));
    public static final TripOption OP3 = new TripOption(new BigDecimal("512.30"));

    private SchedulerHelper(){}

    public static Schedule build4() {
        return new Schedule("1", LocalDate.now(), LocalTime.now(), LocalTime.now().plus(2, HOURS));
    }

    public static Schedule build1_OP1_OP2_OP3() {
        return new Schedule("1", LocalDate.now(), LocalTime.now(), LocalTime.now().plus(2, HOURS), OP1, OP2, OP3);
    }

    public static Schedule build3_OP3() {
        return new Schedule("2", LocalDate.now(), LocalTime.now(), LocalTime.now().plus(2, HOURS), OP3);
    }

    public static Schedule build2_OP3_OP2() {
        return new Schedule("3", LocalDate.now(), LocalTime.now(), LocalTime.now().plus(2, HOURS), OP3, OP2);
    }

}
