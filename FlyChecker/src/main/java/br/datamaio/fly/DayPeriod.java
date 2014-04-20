package br.datamaio.fly;

import static java.time.LocalTime.MAX;
import static java.time.LocalTime.MIN;
import static java.time.LocalTime.NOON;
import static java.time.LocalTime.of;

import java.time.LocalTime;

public enum DayPeriod {
    MORNING (MIN, NOON),
    AFTERNOON (NOON, of(16, 0)),
    NIGHT (of(16, 0), MAX),
    AFTERNOON_OR_NIGHT (NOON, MAX),
    ANY(MIN, MAX);

    public LocalTime begin;
    public LocalTime end;

    private DayPeriod(final LocalTime begin, final LocalTime end) {
        this.begin = begin;
        this.end = end;
    }

    @Override
    public String toString() {
        switch (this) {
            case MORNING:
                return "manhã";
            case AFTERNOON:
                return "tarde";
            case NIGHT:
                return "noite";
        }
        return super.toString();
    }
}
