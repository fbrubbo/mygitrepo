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
    private final List<Option> options = new ArrayList<>();

    public Schedule(final String flyNumber, final LocalDate date, final LocalTime takeoffTime, final Option... ops) {
        this(flyNumber, date, takeoffTime);
        addOptions(ops);
    }

    public Schedule(final String flyNumber, final LocalDate date, final LocalTime takeoffTime) {
        super();
        this.flyNumber = flyNumber;
        this.date = date;
        this.takeoffTime = takeoffTime;
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

    public Option getBestOption() {
        try {
            return options.stream().min( (o1, o2) -> o1.compareTo(o2) ).get();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public List<Option> getOptions() {
        return options;
    }

    public void addOption(final Option op){
        this.options.add(op);
    }

    public void addOptions(final Option... op){
        this.options.addAll(Arrays.asList(op));
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
