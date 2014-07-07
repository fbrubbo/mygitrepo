package br.datamaio.fly;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class Schedule implements Comparable<Schedule> {
    private final String flyNumber;
    private final LocalDate date;
    private final LocalTime takeoffTime;
    private final LocalTime landingTime;
    private final List<TripOption> options = new ArrayList<>();

    public Schedule(final String flyNumber, final LocalDate date, final LocalTime takeoffTime, final LocalTime landingTime, final TripOption... ops) {
        this(flyNumber, date, takeoffTime, landingTime);
        addOptions(ops);
    }

    public Schedule(final String flyNumber, final LocalDate date, final LocalTime takeoffTime, final LocalTime landingTime) {
        super();
        this.flyNumber = flyNumber;
        this.date = date;
        this.takeoffTime = takeoffTime;
        this.landingTime = landingTime;
    }

    public String getFlyNumber() {
        return flyNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public boolean isTakeoffBetween(final DayPeriod period) {
        final LocalTime begin = period.begin;
        final LocalTime end = period.end;
        return (takeoffTime.equals(begin) || takeoffTime.isAfter(begin))
                && (takeoffTime.isBefore(end) || takeoffTime.equals(end));
    }

    public LocalTime getTakeoffTime() {
        return takeoffTime;
    }

    public LocalTime getLandingTime() {
        return landingTime;
    }

    public TripOption getBestOption() {
        try {
            return options.stream().min( (o1, o2) -> o1.compareTo(o2) ).get();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public List<TripOption> getOptions() {
        return options;
    }

    public void addOption(final TripOption op){
        this.options.add(op);
        op.setSchedule(this);
    }

    public void addOptions(final TripOption... op){
        this.options.addAll(Arrays.asList(op));
        for (TripOption option : op) {
            option.setSchedule(this);
        }
    }


    @Override
    public int compareTo(final Schedule o) {
        return getBestOption().compareTo(o.getBestOption());
    }

    @Override
    public String toString() {
        return "Schedule [flyNumber=" + flyNumber
                + ", takeoffTime=" + takeoffTime
                + ", options=" + options + "]";
    }

}
