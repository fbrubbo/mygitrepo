package br.com.datamaio.fwk.criteria;

import java.io.Serializable;

public abstract class Join implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String associationPath;
	private String alias;
	private boolean fetch; 
		
	public Join(String associationPath, boolean fetch)
	{
		this(associationPath, null, fetch);
	}
	
	public Join(String associationPath, String alias, boolean fetch)
	{
		this.associationPath = associationPath;
		if(alias==null)
			this.alias = associationPath;
		else
			this.alias = alias;
		this.fetch = fetch;
	}
		
	public String getAssociationPath() {
		return associationPath;
	}

	public String getAlias() {
		return alias;
	}
	
	public boolean isFetch() {
		return fetch;
	}
	
	/**
	 * Apenas concatena o alias + "." + fieldName
	 * 
	 * @param fieldName o nome do field
	 * @return o path para acesso do field
	 */
	public String pathFor(String fieldName) {
		return alias + "." + fieldName;
	}
	
	public abstract boolean isInnerJoin();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alias == null) ? 0 : alias.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Join other = (Join) obj;
		if (alias == null) {
			if (other.alias != null)
				return false;
		} else if (!alias.equals(other.alias))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return (isInnerJoin()? "INNER JOIN" : "LEFT JOIN" ) 
				+ " [associationPath=" + associationPath
				+ ", alias=" + alias
				+ ", fetch=" + fetch + "]";
	}
	
}