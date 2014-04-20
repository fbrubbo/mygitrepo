package br.datamaio.fly;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Option implements Comparable<Option>{
    private static final NumberFormat REAIS = DecimalFormat.getCurrencyInstance();

    private Schedule schedule;
    private final String type;
    private final BigDecimal value;

    public Option(final BigDecimal value) {
        this(null, value);
    }

    public Option(final Schedule schedule, final BigDecimal value) {
        this(schedule, null, value);
    }

    public Option(final Schedule schedule, final String type, final BigDecimal value) {
        super();
        this.schedule = schedule;
        this.type = type;
        this.value = value;
    }

    public Schedule getSchedule(){
        return schedule;
    }

    void setSchedule(final Schedule schedule){
        this.schedule = schedule;
    }

    public String getType() {
        return type;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public int compareTo(final Option o) {
        if(o==null) {
            return -1;
        }
        return value.compareTo(o.value);
    }

    @Override
    public String toString() {
        return "Option [type=" + type + ", value=" + (value!=null ? REAIS.format(value) : null) + "]";
    }

}
