package br.com.datamaio.fwk.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public abstract class BasicDao {
	@Inject 
	protected EntityManager em;
	
	/**
	 * <b>Evite usar este comando. Use apenas em casos extremamente necessários</b><br>
	 * 
	 * Este método força o hibernate enviar os comandos SQL (insert, update, delete) da sessão para o banco de dados.
	 */
	public void flush(){
		em.flush();
	}
	
	/**
	 * <b>Evite usar este comando. Use apenas em casos extremamente necessários</b><br>
	 * 
	 * Este método limpa todo o cache do hibernate. Isto é, todos objetos atachados passam a ser desatachados<br>
	 * <b>OBS: SE O CONTEÚDO DESTA ENTIDADE TIVER SIDO ALTERADA, ESTA ALTERAÇÃO SERÁ PERDIDA</B>
	 * 
	 * @see {@link #flushAndDetachAll()}</b>
	 */
	public void detachAll(){
		em.clear();
	} 

	/**
	 * <b>Evite usar este comando. Use apenas em casos extremamente necessários</b><br>
	 * 
	 * Antes de desatachar todas entidades força o hibernate a executar todos os comandos sql pendentes  
	 */
	public void flushAndDetachAll(){
		flush();
		detachAll();
	}
	
	/**
	 * <b>Evite usar este comando. Use apenas em casos extremamente necessários</b><br>
	 * 
	 * Este método transforma a entidade atachada passada por parâmetro em desatachada. <br>
	 * <b>OBS: SE O CONTEÚDO DESTA ENTIDADE TIVER SIDO ALTERADA, ESTA ALTERAÇÃO SERÁ PERDIDA</B>
	 * 
	 * @see {@link #flushAndDetach(Object)}</b>
	 */
	public void detach(Object entity){
		em.detach(entity);
	}

	/**
	 * <b>Evite usar este comando. Use apenas em casos extremamente necessários</b><br>
	 * 
	 * Antes de desatachar a entidade força o hibernate a executar todos os comandos sql pendentes  
	 */
	public void flushAndDetach(Object entity){
		flush();
		detach(entity);		
	}	
}
