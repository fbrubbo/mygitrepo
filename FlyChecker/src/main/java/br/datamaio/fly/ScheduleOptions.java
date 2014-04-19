package br.datamaio.fly;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class ScheduleOptions {
    private final List<Schedule> schedules = new ArrayList<>();

    public ScheduleOptions(final Schedule... schs){
        add(schs);
    }

    public void add(final Schedule... schs) {
        add(Arrays.asList(schs));
    }

    public void add(final List<Schedule> schs) {
        this.schedules.addAll(schs);
    }

    public void add(final Schedule sch) {
        schedules.add(sch);
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public Schedule getBestSchedule(){
        try {
            Schedule schedule = schedules.stream()
                    .min((s1, s2)-> s1.compareTo(s2)).get();
            if (schedule.getBestOption()==null) {
                return null;
            }
            return schedule;
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public Option getBestOption(final LocalTime at){
        try {
            Schedule schedule = schedules.stream()
                    .filter(s->s.getTakeoffTime().equals(at))
                    .min((s1, s2)-> s1.compareTo(s2))
                    .get();
            if (schedule.getBestOption()==null) {
                return null;
            }
            return schedule.getBestOption();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public Option getBestOption(final LocalTime begin, final LocalTime end){
        try {
            Schedule schedule = schedules.stream()
                    .filter(s-> s.isTakeoffBetween(begin, end))
                    .min((s1, s2)-> s1.compareTo(s2))
                    .get();
            if (schedule.getBestOption()==null) {
                return null;
            }
            return schedule.getBestOption();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

}
