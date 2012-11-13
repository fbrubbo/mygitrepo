package br.com.datamaio.fwk.dao;

import java.util.List;

import br.com.datamaio.fwk.criteria.BasicCriteria;
import br.com.datamaio.fwk.entity.BasicEntity;

public class CrudDao<E extends BasicEntity, C extends BasicCriteria> extends SearchableDao<E, C>{

	public CrudDao() {
	}
	
	public CrudDao(Class<E> entityClass) {
		super(entityClass);
	}
	
	protected E saveOrUpdate(E entity) {
		if (entity.getId() == null) {
			em.persist(entity);
			return entity;
		}
		return em.merge(entity);
	}

	public void remove(List<Long> ids) {
		for (Long id : ids) {
			remove(id);
		}
	}
	
	public void remove(E entity) {
		remove(entity.getId());
	}
	
	public void remove(Long id) {
		E e = em.find(this.entityClass, id);
		em.remove(e);
	}
}
