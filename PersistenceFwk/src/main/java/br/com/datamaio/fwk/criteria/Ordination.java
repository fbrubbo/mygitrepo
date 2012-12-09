package br.com.datamaio.fwk.criteria;

import java.io.Serializable;

public class Ordination implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String propertyName;
	private boolean ascending;

	public Ordination(String propertyName) {
		this(propertyName, true);
	}
	
	public Ordination(String propertyName, boolean ascending) {
		super();
		this.propertyName = propertyName;
		this.ascending = ascending;
	}	
	
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public boolean isAscending() {
		return ascending;
	}
	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((propertyName == null) ? 0 : propertyName.hashCode());
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
		Ordination other = (Ordination) obj;
		if (propertyName == null) {
			if (other.propertyName != null)
				return false;
		} else if (!propertyName.equals(other.propertyName))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Order [propertyName=" + propertyName + ", ascending="
				+ ascending + "]";
	}
	
}