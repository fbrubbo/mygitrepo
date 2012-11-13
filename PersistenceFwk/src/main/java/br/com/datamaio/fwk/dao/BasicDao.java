package br.com.datamaio.fwk.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public abstract class BasicDao {
	@Inject 
	protected EntityManager em;
	
	/**
	 * Cuidado no uso.<Br>
	 * Este m�todo limpa todo o cache do hibernate.
	 * Isto �, todos objetos atachados passam a ser desatachados
	 */
	public void clear(){
		em.clear();
	} 
		
	/**
	 * Este m�todo transforma a entidade atachada passada por par�metro
	 * em desatachada
	 */
	public void detach(Object entity){
		em.detach(entity);
	}
	
	/**
	 * Este m�todo for�a o hibernate enviar os comandos SQL (insert, update, 
	 * delete) para o banco de dados.
	 */
	public void flush(){
		em.flush();
	}
	
	public void flushAndDetach(Object entity){
		flush();
		detach(entity);		
	}
	
	public void flushAndClear(){
		flush();
		clear();
	}
}
