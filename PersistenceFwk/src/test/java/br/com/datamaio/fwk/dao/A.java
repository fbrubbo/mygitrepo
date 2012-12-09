package br.com.datamaio.fwk.dao;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import br.com.datamaio.fwk.entity.BasicEntity;

@Entity
public class A extends BasicEntity {
	@Id private Long id;
	@Basic private String nome;
	@OneToMany(fetch=FetchType.LAZY, mappedBy="a") private Set<B> bs;

	public A(){}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Set<B> getBs() {
		return bs;
	}
	public void setBs(Set<B> bs) {
		this.bs = bs;
		for (B b : bs) {
			b.setA(this);
		}
	}
	public void addB(B b){
		if(this.bs!=null)
			this.bs = new HashSet<B>();
		this.bs.add(b);
		b.setA(this);
	}
}