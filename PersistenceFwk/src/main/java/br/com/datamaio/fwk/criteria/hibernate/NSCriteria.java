package br.com.datamaio.fwk.criteria.hibernate;

import java.util.List;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.transform.ResultTransformer;

/**
 * Esta classe encapsula a lógica de <code>Null Safe</code> para o {@link CriteriaImpl} do hibernate.<br>
 * Para mais detalhes, ver documentação do hibernate.
 * 
 * @author Fernando Rubbo
 */
public class NSCriteria<T> extends CriteriaImpl 
{
	private static final long serialVersionUID = 1L;
	
	
	public NSCriteria(Class<T> clazz, SessionImplementor session) {
		super(clazz.getName(), session);
	}

	public NSCriteria(Class<T> clazz, String alias, SessionImplementor session)
	{
		super(clazz.getName(), alias, session);	
	}
	
	@Override
	public NSCriteria<T> add(Criteria criteriaInst, Criterion expression) {
		if(expression!=null)
			super.add(criteriaInst, expression);

		return this;
	}
	
	@Override
	public NSCriteria<T> add(Criterion expression) {
		if(expression!=null)
			super.add(expression);
		
		return this;
	}
	
	@Override
	public NSCriteria<T> addOrder(Order ordering) {
		if(ordering!=null)
			super.addOrder(ordering);
		
		return this;
	}

	@Override
	public NSCriteria<T> setFetchMode(String associationPath, FetchMode mode) {
		if(associationPath!=null)
			super.setFetchMode(associationPath, mode);
		
		return this;
	}

	@Override
	public NSCriteria<T> setLockMode(LockMode lockMode) {
		super.setLockMode(lockMode);
		return this;
	}

	@Override
	public NSCriteria<T> setLockMode(String alias, LockMode lockMode) {
		super.setLockMode(alias, lockMode);
		return this;
	}
	
	@Override
	public NSCriteria<T> setFetchSize(int fetchSize) {
		super.setFetchSize(fetchSize);
		return this;
	}
	
	@Override
	public NSCriteria<T> setTimeout(int timeout) {
		super.setTimeout(timeout);
		return this;
	}
	
	@Override
	public NSCriteria<T> setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);		
		return this;
	}
	
	@Override
	public NSCriteria<T> setCacheable(boolean cacheable) {
		super.setCacheable(cacheable);
		return this;
	}
	
	@Override
	public NSCriteria<T> setCacheRegion(String cacheRegion) {
		super.setCacheRegion(cacheRegion);
		return this;
	}
	
	@Override
	public NSCriteria<T> setComment(String comment) {
		super.setComment(comment);
		return this;
	}

	@Override
	public NSCriteria<T> setFlushMode(FlushMode flushMode) {
		super.setFlushMode(flushMode);
		return this;
	}

	@Override
	public NSCriteria<T> setCacheMode(CacheMode cacheMode) {
		super.setCacheMode(cacheMode);
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> list() throws HibernateException {
		return super.list();
	}
	
	@Override	
	public NSCriteria<T> setMaxResults(int max){
		super.setMaxResults(max);		
		return this;	
	}
	
	@Override
	public NSCriteria<T> setFirstResult(int first){
		super.setFirstResult(first);		
		return this;	
	}
	
	@Override
	public NSCriteria<T> setResultTransformer(ResultTransformer tupleMapper) {
		super.setResultTransformer(tupleMapper);
		return this;
	}
	
	@Override
	public NSCriteria<T> setProjection(Projection projection) {
		super.setProjection(projection);
		return this;
	}
}
