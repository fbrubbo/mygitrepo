package br.datamaio.fly;

import java.math.BigDecimal;

public class Option implements Comparable<Option>{
    private final String type;
    private final BigDecimal value;

    public Option(final BigDecimal value) {
        this(null, value);
    }

    public Option(final String type, final BigDecimal value) {
        super();
        this.type = type;
        this.value = value;
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
        return "Option [type=" + type + ", value=" + value + "]";
    }

}
