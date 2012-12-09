package br.com.datamaio.fwk.dao;

import br.com.datamaio.fwk.criteria.BasicCriteria;
import br.com.datamaio.fwk.criteria.InnerJoin;

public class ACriteria extends BasicCriteria {
	private String nome;
	private String bnome;
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getBnome() {
		return bnome;
	}
	public void setBnome(String bnome) {
		this.bnome = bnome;
		this.addJoin(new InnerJoin("bs", "bss", false));
	}
}