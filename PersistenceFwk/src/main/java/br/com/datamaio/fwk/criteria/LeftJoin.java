package br.com.datamaio.fwk.criteria;

public class LeftJoin extends Join {

	private static final long serialVersionUID = 1L;
	
	public LeftJoin(String associationPath) {
		this(associationPath, true);
	}
	
	public LeftJoin(String associationPath, boolean fetch) {
		super(associationPath, fetch);
	}

	public LeftJoin(String associationPath, String alias) {
		this(associationPath, alias, true);
	}
	
	public LeftJoin(String associationPath, String alias, boolean fetch) {
		super(associationPath, alias, fetch);
	}

	@Override
	public boolean isInnerJoin() {
		return false;
	}
	
}