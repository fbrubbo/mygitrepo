package br.com.chai.domain;

import java.math.BigDecimal;

import br.com.chai.util.NumberUtil;

public class YearConsumption {
    private int year;
    private BigDecimal numMonths;
    private BigDecimal predictedConsumption;

    public YearConsumption(final int year, final BigDecimal numMonths, final BigDecimal predictedConsumption) {
        super();
        this.year = year;
        this.numMonths = numMonths;
        this.predictedConsumption = predictedConsumption;
    }

    public int getYear() {
        return year;
    }
    public void setYear(final int year) {
        this.year = year;
    }
    public BigDecimal getNumMonths() {
        return numMonths;
    }
    public void setNumMonths(final BigDecimal numMonths) {
        this.numMonths = numMonths;
    }
    public BigDecimal getPredictedConsumption() {
        return predictedConsumption;
    }
    public void setPredictedConsumption(final BigDecimal predictedConsumption) {
        this.predictedConsumption = predictedConsumption;
    }

    @Override
    public String toString() {
        return "Ano=" + year + " | número de meses=" + numMonths + " | consumo previsto=" + NumberUtil.format(predictedConsumption, 2, "R$") + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((numMonths == null) ? 0 : numMonths.hashCode());
        result = prime * result + ((predictedConsumption == null) ? 0 : predictedConsumption.hashCode());
        result = prime * result + year;
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        YearConsumption other = (YearConsumption) obj;
        if (numMonths == null) {
            if (other.numMonths != null) {
                return false;
            }
        } else if (!numMonths.equals(other.numMonths)) {
            return false;
        }
        if (predictedConsumption == null) {
            if (other.predictedConsumption != null) {
                return false;
            }
        } else if (!predictedConsumption.equals(other.predictedConsumption)) {
            return false;
        }
        if (year != other.year) {
            return false;
        }
        return true;
    }


}
