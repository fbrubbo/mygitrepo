package br.com.datamaio.fwk.dao;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import br.com.datamaio.fwk.entity.BasicEntity;

@Entity
public class C extends BasicEntity {
	@Id private Long id;
	@Basic private String nome;
	@ManyToOne(fetch=FetchType.LAZY) private B b;
	
	public C(){}
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
	public B getB() {
		return b;
	}
	public void setB(B b) {
		this.b = b;
	}		
}