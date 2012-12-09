package br.com.datamaio.fwk.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public abstract class BasicDao {
	@Inject 
	protected EntityManager em;
	
	/**
	 * <b>Evite usar este comando. Use apenas em casos extremamente necess�rios</b><br>
	 * 
	 * Este m�todo for�a o hibernate enviar os comandos SQL (insert, update, delete) da sess�o para o banco de dados.
	 */
	public void flush(){
		em.flush();
	}
	
	/**
	 * <b>Evite usar este comando. Use apenas em casos extremamente necess�rios</b><br>
	 * 
	 * Este m�todo limpa todo o cache do hibernate. Isto �, todos objetos atachados passam a ser desatachados<br>
	 * <b>OBS: SE O CONTE�DO DESTA ENTIDADE TIVER SIDO ALTERADA, ESTA ALTERA��O SER� PERDIDA</B>
	 * 
	 * @see {@link #flushAndDetachAll()}</b>
	 */
	public void detachAll(){
		em.clear();
	} 

	/**
	 * <b>Evite usar este comando. Use apenas em casos extremamente necess�rios</b><br>
	 * 
	 * Antes de desatachar todas entidades for�a o hibernate a executar todos os comandos sql pendentes  
	 */
	public void flushAndDetachAll(){
		flush();
		detachAll();
	}
	
	/**
	 * <b>Evite usar este comando. Use apenas em casos extremamente necess�rios</b><br>
	 * 
	 * Este m�todo transforma a entidade atachada passada por par�metro em desatachada. <br>
	 * <b>OBS: SE O CONTE�DO DESTA ENTIDADE TIVER SIDO ALTERADA, ESTA ALTERA��O SER� PERDIDA</B>
	 * 
	 * @see {@link #flushAndDetach(Object)}</b>
	 */
	public void detach(Object entity){
		em.detach(entity);
	}

	/**
	 * <b>Evite usar este comando. Use apenas em casos extremamente necess�rios</b><br>
	 * 
	 * Antes de desatachar a entidade for�a o hibernate a executar todos os comandos sql pendentes  
	 */
	public void flushAndDetach(Object entity){
		flush();
		detach(entity);		
	}	
}
