package br.com.datamaio.fwk.criteria;

public class InnerJoin extends Join {

	private static final long serialVersionUID = 1L;

	public InnerJoin(String associationPath) {
		this(associationPath, true);
	}
	
	public InnerJoin(String associationPath, boolean fetch) {
		super(associationPath, fetch);
	}

	public InnerJoin(String associationPath, String alias) {
		this(associationPath, alias, true);
	}
	
	public InnerJoin(String associationPath, String alias, boolean fetch) {
		super(associationPath, alias, fetch);
	}

	@Override
	public boolean isInnerJoin() {
		return true;
	}
	
}