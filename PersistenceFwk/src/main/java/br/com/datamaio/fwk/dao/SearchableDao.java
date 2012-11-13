package br.com.datamaio.fwk.dao;

import java.lang.reflect.Field;
import java.util.List;

import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.SessionException;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.engine.spi.SessionImplementor;

import br.com.datamaio.fwk.criteria.BasicCriteria;
import br.com.datamaio.fwk.criteria.Join;
import br.com.datamaio.fwk.criteria.OrderCriteria;
import br.com.datamaio.fwk.criteria.ann.Restriction;
import br.com.datamaio.fwk.criteria.hibernate.NSCriteria;
import br.com.datamaio.fwk.entity.BasicEntity;
import br.com.datamaio.fwk.util.HibernateUtil;
import br.com.datamaio.fwk.util.ReflectionUtil;
import br.com.datamaio.fwk.util.StringUtil;

public class SearchableDao<E extends BasicEntity, C extends BasicCriteria> extends BasicDao {
	
	protected Class<E> entityClass; 

	public SearchableDao() {
		this.entityClass = HibernateUtil.inferEntityClass(this, SearchableDao.class);
	}
	
	public SearchableDao(Class<E> entityClass) {
		this.entityClass = entityClass;
	}	

	public NSCriteria<E> createNSCriteria() {
		return createNSCriteria(entityClass);
	}
	
	public <T> NSCriteria<T> createNSCriteria(Class<T> persistentClass, String alias) {
		errorIfClosed();
		SessionImplementor session = (SessionImplementor) em.getDelegate();
		return new NSCriteria<T>(persistentClass, alias, session);
	}

	public <T> NSCriteria<T> createNSCriteria(Class<T> persistentClass) {
		errorIfClosed();
		SessionImplementor session = (SessionImplementor) em.getDelegate();
		return new NSCriteria<T>(persistentClass, session);
	}
	
	public E findById(Integer id) {
		return em.find(this.entityClass, id);
	}

	public boolean exists(final C criteria) {
		return findBy(criteria).size()>0;
	}
	
	public List<E> findBy(final C criteria) {
		final NSCriteria<E> nsCrit = createNSCriteria()
				.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		
		addJoins(nsCrit, criteria);
		addRestrictions(nsCrit, criteria);
		addOrdination(nsCrit, criteria);
		setLimitAndOffset(nsCrit, criteria);

		return nsCrit.list();
	}

	public Long count(final C criteria) {
		final NSCriteria<E> nsCrit = createNSCriteria()
				.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		// TODO: aparentemente este addFetchs não está funcionando para o
		// count..
		// isto pode ser um problema no futuro
		addJoins(nsCrit, criteria);
		addRestrictions(nsCrit, criteria);

		return (Long) nsCrit.setProjection(Projections.rowCount())
				.uniqueResult();
	}
	
	/**
	 * Utilizado pelos métodos {@link #findBy(BasicCriteria)} e {@link #count(BasicCriteria)}
	 * para preencher os filtros da pesquisa
	 * 
	 * @param nsCrit implementação <code>Null Safe</code> do criteria do hibernate.
	 * @param criteria objeto de filtro utilizado para montar o where
	 */
	protected void addRestrictions(final NSCriteria<E> nsCrit, final C criteria) {
		// default: do nothing
		final List<Field> fields = ReflectionUtil.getDeclaredFields(criteria.getClass());
		for (Field field : fields) {
			final Object value = ReflectionUtil.getProperty(criteria, field.getName());
			if(value !=null  && field.isAnnotationPresent(Restriction.class)){
				final Restriction rest = field.getAnnotation(Restriction.class);
				final String prop = StringUtil.isEmpty(rest.prop()) ? field.getName() : rest.prop();
				final String criterion = rest.criterion();
				
				// TODO: tratar os casos diferentes de String, object.. por exemplo: between.. isnull, etc
				Criterion expression = (Criterion)ReflectionUtil.invokeStaticMethod(NSCriteria.class, criterion, new Object[]{prop, value});
				nsCrit.add(expression);
			}
		}
	}

	private void addJoins(final NSCriteria<E> nsCrit, final C criteria) {
		for (Join join : criteria.getJoins()) {
			final String path = join.getAssociationPath();

			final String alias = join.getAlias();
			final int joinType = join.isInnerJoin() ? 0 : 1;
			nsCrit.createAlias(path, alias, joinType);

			// FIXME: Se eu crio um alias, o hibernate ignora o fetch mode
			// OBS: Parece que o nHibernate tem o mesmo problema.
			// http://stackoverflow.com/questions/5296461/nhibernate-how-to-make-criteria-inner-join-without-hydrating-objects
			if (join.isFetch())
				nsCrit.setFetchMode(path, FetchMode.JOIN);
			else
				nsCrit.setFetchMode(path, FetchMode.SELECT);
				//nsCrit.setFetchMode(path, FetchMode.DEFAULT);
		}
	}

	private void addOrdination(final NSCriteria<E> nsCrit, final C criteria) {
		for (OrderCriteria order : criteria.getOrderColumns()) {
			final String name = order.getPropertyName();
			nsCrit.addOrder(order.isAscending() ? Order.asc(name) : Order.desc(name));
		}
	}

	private void setLimitAndOffset(final NSCriteria<E> nsCrit, final C criteria) {
		final Integer page = criteria.getPage();
		final Integer size = criteria.getPageSize();
		if (page != null && size != null) {
			final int firstResult = page * size;
			nsCrit.setFirstResult(firstResult).setMaxResults(size);
		}
	}

	private void errorIfClosed() {
		Session session = (Session) em.getDelegate();
		if (!session.isOpen())
			throw new SessionException("Session is closed!");
	}
}
