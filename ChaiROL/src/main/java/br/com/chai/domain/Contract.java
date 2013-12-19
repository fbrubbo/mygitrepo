package br.com.chai.domain;

import static java.math.RoundingMode.CEILING;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.chai.util.DateUtil;
import br.com.chai.util.NumberUtil;

public class Contract implements Comparable<Contract>{
//    Empr    Doc.compra  N. Acomp    Tipo    Sua ref.    Fornecedor  Nº conta do fornecedor  InPerVal    Inicio consumo da ROL   Fim da validade ValFix.(cabeç.) consumo / mês
//    D001    4600017078  ASC         WK      S034        1025433 TELEFONICA BRASIL S.A.  01.06.2009  01.01.2011  31.03.2013      2.665.205,97    98.711,33
    private String empr;
    private String docCompra;
    private String nrAcomp;
    private String tipo;
    private String suaRef;
    private String fornecedor;
    private String nrContaFornecedor;
    private String inPerVal;
    private Calendar inicioConsumoROL;
    private Calendar fimConsumoROL;
    private BigDecimal vlrFixo;


    public String getEmpr() {
        return empr;
    }
    public void setEmpr(final String empr) {
        this.empr = empr;
    }
    public String getDocCompra() {
        return docCompra;
    }
    public void setDocCompra(final String docCompra) {
        this.docCompra = docCompra;
    }
    public String getNrAcomp() {
        return nrAcomp;
    }
    public void setNrAcomp(final String nrAcomp) {
        this.nrAcomp = nrAcomp;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(final String tipo) {
        this.tipo = tipo;
    }
    public String getSuaRef() {
        return suaRef;
    }
    public void setSuaRef(final String suaRef) {
        this.suaRef = suaRef;
    }
    public String getFornecedor() {
        return fornecedor;
    }
    public void setFornecedor(final String fornecedor) {
        this.fornecedor = fornecedor;
    }
    public String getNrContaFornecedor() {
        return nrContaFornecedor;
    }
    public void setNrContaFornecedor(final String nrContaFornecedor) {
        this.nrContaFornecedor = nrContaFornecedor;
    }
    public String getInPerVal() {
        return inPerVal;
    }
    public void setInPerVal(final String inPerVal) {
        this.inPerVal = inPerVal;
    }
    public Calendar getInicioConsumoROL() {
        return inicioConsumoROL;
    }
    public void setInicioConsumoROL(final Calendar inicioConsumoROL) {
        this.inicioConsumoROL = inicioConsumoROL;
    }
    public void setInicioConsumoROL(final String str) {
        this.inicioConsumoROL = DateUtil.parseBegin(str);
    }
    public Calendar getFimConsumoROL() {
        return fimConsumoROL;
    }
    public void setFimConsumoROL(final Calendar fimConsumoROL) {
        this.fimConsumoROL = fimConsumoROL;
    }
    public void setFimConsumoROL(final String str) {
        this.fimConsumoROL = DateUtil.parseEnd(str);
    }
    public BigDecimal getQuantidadeTotalMeses(){
        return DateUtil.monthDiff(this.inicioConsumoROL, this.fimConsumoROL);
    }
    public BigDecimal getVlrFixo() {
        return vlrFixo;
    }
    public void setVlrFixo(final BigDecimal vlrFixo) {
        this.vlrFixo = vlrFixo;
    }
    public void setVlrFixo(final String str) {
        this.vlrFixo = NumberUtil.parse(str);
    }
    public BigDecimal getConsumoMes(){
        return this.vlrFixo.divide(getQuantidadeTotalMeses(), 2, CEILING);
    }
    public List<YearConsumption> getYearConsumption(){
        List<YearConsumption> years = new ArrayList<YearConsumption>();

        int year = this.inicioConsumoROL.get(Calendar.YEAR);
        int endYear = this.fimConsumoROL.get(Calendar.YEAR);
        while(year<=endYear) {
            BigDecimal months = DateUtil.monthDiffInYear(this.inicioConsumoROL, this.fimConsumoROL, year);
            BigDecimal predictedConsumption = getConsumoMes().multiply(months).setScale(2, CEILING);
            years.add(new YearConsumption(year, months, predictedConsumption));
            year++;
        }

        return years;
    }

    @Override
    public int compareTo(final Contract o) {
        return this.empr.compareTo(o.empr);
    }
}
