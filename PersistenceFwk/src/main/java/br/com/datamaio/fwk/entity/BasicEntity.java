package br.com.datamaio.fwk.entity;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

import org.hibernate.Hibernate;

import br.com.datamaio.fwk.util.DateUtil;
import br.com.datamaio.fwk.util.HibernateUtil;
import br.com.datamaio.fwk.util.ReflectionUtil;

@MappedSuperclass
public abstract class BasicEntity implements Serializable, Comparable<BasicEntity> {
	private static final long serialVersionUID = 1L;

	public abstract Long getId();
	 
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!HibernateUtil.isSameClass(this, obj))
			return false;
		BasicEntity other = (BasicEntity) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}


	/**
	 * Este método deve ser utilizado apenas para fazer o debugging da
	 * aplicação. Visto que sua implementação é fortemente baseada em reflexão,
	 * o desenvolvedor deve fazer o seguinte para evitar possíveis problemas de
	 * performance:
	 * 
	 * <pre>
	 * if (LOGGER.isDebugEnabled()) {
	 * 	LOGGER.debug(&quot;Conteúdo da entidade: &quot; + entity.toString());
	 * }
	 * </pre>
	 * 
	 * Para uma implementação mais performática, o desenvolvedor deve
	 * sobrescre essvere método de acordo com suas necessidades.
	 * 
	 * @return uma representação desta entidade.
	 */
	@Override
	public String toString() {
		PropertyDescriptor[] descriptors = ReflectionUtil.getPropertyDescriptors(this.getClass());

		StringBuilder buff = new StringBuilder(descriptors.length * 10);
		buff.append(this.getClass().getSimpleName()).append(" = [id:").append(getId());

		for (PropertyDescriptor descriptor : descriptors) {
			String name = descriptor.getName();
			if ("class".equals(name) || "id".equals(name) || "version".equals(name)) {
				continue;
			}

			Method method = descriptor.getReadMethod();

			if (Collection.class.isAssignableFrom(method.getReturnType())) {
				buff.append(", ").append(name).append(":").append("{Collection N/A}");
			} else {
				ManyToOne manyToOne = method.getAnnotation(ManyToOne.class);
				OneToOne oneToOne = method.getAnnotation(OneToOne.class);
				Basic basic = method.getAnnotation(Basic.class);
				if (isManyToOneLazy(manyToOne) || isOneToOneLazy(oneToOne) || isBasicLazy(basic) || isNotInformedNeitherOfThem(manyToOne, oneToOne, basic)) {
					buff.append(", ").append(name).append(":");
					Object obj = ReflectionUtil.invokeMethod(method, this);
					if (obj instanceof Calendar) {
						buff.append(DateUtil.format((Calendar) obj));
					} else {
						buff.append(obj);
					}
				} else {
					buff.append(", ").append(name).append(":").append("{Lazy Property}");
				}
			}
		}

		buff.append("]");
		return buff.toString();
	}

	/**
	 * Compara duas entidades pelo ID
	 * 
	 * @param entity entidade a ser comparada
	 * @return <b>-1</b> se a entidade corrente possui o menor ID; <b>0</b> se
	 *         os IDs são iguais e; <b>1</b> se a entidade corrente possui o
	 *         maior ID
	 */
	@Override
	public int compareTo(BasicEntity entity) {
		return this.getId().compareTo(entity.getId());
	}

	private boolean isNotInformedNeitherOfThem(ManyToOne manyToOne, OneToOne oneToOne, Basic basic) {
		return ((manyToOne == null) && (oneToOne == null) && (basic == null));
	}

	private boolean isBasicLazy(Basic basic) {
		return ((basic != null) && !FetchType.LAZY.equals(basic.fetch()));
	}

	private boolean isOneToOneLazy(OneToOne oneToOne) {
		return ((oneToOne != null) && !FetchType.LAZY.equals(oneToOne.fetch()));
	}

	private boolean isManyToOneLazy(ManyToOne manyToOne) {
		return ((manyToOne != null) && !FetchType.LAZY.equals(manyToOne.fetch()));
	}
}
