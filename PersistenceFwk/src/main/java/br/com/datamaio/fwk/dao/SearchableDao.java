package br.com.datamaio.fwk.dao;

import static org.hibernate.criterion.CriteriaSpecification.DISTINCT_ROOT_ENTITY;
import static org.hibernate.sql.JoinType.INNER_JOIN;
import static org.hibernate.sql.JoinType.LEFT_OUTER_JOIN;

import java.util.List;

import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.SessionException;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.sql.JoinType;

import br.com.datamaio.fwk.criteria.BasicCriteria;
import br.com.datamaio.fwk.criteria.Join;
import br.com.datamaio.fwk.criteria.Ordination;
import br.com.datamaio.fwk.criteria.hibernate.NSCriteria;
import br.com.datamaio.fwk.entity.BasicEntity;
import br.com.datamaio.fwk.util.HibernateUtil;

public class SearchableDao<E extends BasicEntity, C extends BasicCriteria> extends BasicDao {
	
	/** Default alias para a entidade principal */
	protected static final String ROOT_ALIAS = CriteriaSpecification.ROOT_ALIAS;
	protected Class<E> entityClass; 

	/** Construtor padrão. Infere a o <code><E></code> do DAO sendo usado */
	public SearchableDao() {
		this.entityClass = HibernateUtil.inferEntityClass(this, SearchableDao.class);
	}
	
	/** Construtor padrão que recebe o <code><E></code> */
	public SearchableDao(Class<E> entityClass) {
		this.entityClass = entityClass;
	}	

	/** Cria um Null Safe Criteria para a entidade que este DAO representa*/
	public NSCriteria<E> createNSCriteria() {
		return createNSCriteria(ROOT_ALIAS);
	}
	
	/** Cria um Null Safe Criteria para a entidade que este DAO representa*/
	public NSCriteria<E> createNSCriteria(String alias) {
		return createNSCriteria(entityClass, alias);
	}
	
	/** Cria um Null Safe Criteria com uma dada entidade e um alias */
	public <T> NSCriteria<T> createNSCriteria(Class<T> persistentClass, String alias) {
		errorIfClosed();
		SessionImplementor session = (SessionImplementor) em.getDelegate();
		return new NSCriteria<T>(persistentClass, alias, session);
	}

	/** Pesquisa por id */
	public E findById(Long id) {
		return em.find(this.entityClass, id);
	}

	/** Verifica se uma determinada entidade existe */
	public boolean exists(Long id) {
		return findById(id)!=null;
	}
	
	/** Verifica se uma determinada consulta retornou algum resultado */
	public boolean exists(final C criteria) {
		return findBy(criteria).size()>0;
	}
	
	/** Pesquisa por critério */
	public List<E> findBy(final C criteria) {
		final NSCriteria<E> nsCrit = createNSCriteria()
				.setResultTransformer(DISTINCT_ROOT_ENTITY); //TODO: deixar o distinct ou não?!?!?
		
		addJoins(nsCrit, criteria);
		addRestrictions(nsCrit, criteria);
		addOrderBy(nsCrit, criteria);
		setLimitAndOffset(nsCrit, criteria);

		return nsCrit.list();
	}

	/** Executa o count */
	public Long count(final C criteria) {
		final NSCriteria<E> nsCrit = createNSCriteria()
				.setProjection(Projections.rowCount())
				.setResultTransformer(DISTINCT_ROOT_ENTITY); //TODO: deixar o distinct ou não?!?!?

		// TODO: aparentemente este addFetchs não está funcionando para o count..
		// isto pode ser um problema no futuro
		addJoins(nsCrit, criteria);
		addRestrictions(nsCrit, criteria);

		return (Long) nsCrit.uniqueResult();
	}
	
	/**
	 * Utilizado pelos métodos {@link #findBy(BasicCriteria)} e {@link #count(BasicCriteria)}
	 * para preencher os filtros da pesquisa
	 * 
	 * @param nsCrit implementação <code>Null Safe</code> do criteria do hibernate.
	 * @param criteria objeto de filtro utilizado para montar o where
	 */
	protected void addRestrictions(final NSCriteria<E> nsCrit, final C criteria) {
	}

	private void addJoins(final NSCriteria<E> nsCrit, final C criteria) {
		for (Join join : criteria.getJoins()) {
			final String associationPath = join.getAssociationPath();

			final String alias = join.getAlias();
			final JoinType joinType = join.isInnerJoin() ? INNER_JOIN : LEFT_OUTER_JOIN;
			nsCrit.createAlias(associationPath, alias, joinType);

			// FIXME: Se eu crio um alias, o hibernate ignora o fetch mode
			// OBS: Parece que o nHibernate tem o mesmo problema.
			// http://stackoverflow.com/questions/5296461/nhibernate-how-to-make-criteria-inner-join-without-hydrating-objects
			if (join.isFetch())
				nsCrit.setFetchMode(associationPath, FetchMode.JOIN);
			else
				nsCrit.setFetchMode(associationPath, FetchMode.DEFAULT);
		}
	}

	private void addOrderBy(final NSCriteria<E> nsCrit, final C criteria) {
		for (Ordination order : criteria.getOrdinations()) {
			final String propertyName = order.getPropertyName();
			nsCrit.addOrder(order.isAscending() ? Order.asc(propertyName) : Order.desc(propertyName));
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
		final Session session = (Session) em.getDelegate();
		if (!session.isOpen())
			throw new SessionException("Session is closed!");
	}
}
