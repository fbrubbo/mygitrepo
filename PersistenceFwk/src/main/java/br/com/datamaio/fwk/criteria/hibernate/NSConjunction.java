package br.com.datamaio.fwk.criteria.hibernate;

import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Junction;

/**
 * Esta classe encapsula a l�gica de <code>Null Safe</code> para o {@link Conjunction} do hibernate.<br>
 * Para mais detalhes, ver documenta��o do hibernate.
 * 
 * @author Fernando Rubbo
 */
public class NSConjunction extends Conjunction {

	private static final long serialVersionUID = 1L;

	@Override
	public Junction add(Criterion criterion) {
		if(criterion!=null){
			super.add(criterion);
		}
		return this;
	}
}
