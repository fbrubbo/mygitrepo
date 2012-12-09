package br.com.datamaio.fwk.dao;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import br.com.datamaio.fwk.entity.BasicEntity;

@Entity
public class B extends BasicEntity {
	@Id private Long id;
	@Basic private String nome;
	@ManyToOne(fetch=FetchType.LAZY) private A a;
	@OneToMany(fetch=FetchType.LAZY, mappedBy="b") private Set<C> cs;
	
	public B(){}
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
	public A getA() {
		return a;
	}
	public void setA(A a) {
		this.a = a;
	}	
	public Set<C> getCs() {
		return cs;
	}
	public void setBs(Set<C> cs) {
		this.cs = cs;
		for (C c : cs) {
			c.setB(this);
		}
	}
	public void addB(C c){
		if(this.cs!=null)
			this.cs = new HashSet<C>();
		this.cs.add(c);
		c.setB(this);
	}
}