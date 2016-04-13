package io.skysail.server.security.config;

public class StartsWithPathExpression implements PathExpression {

	private String startsWith;

	private boolean permitAll;

	public StartsWithPathExpression(String startsWith) {
		this.startsWith = startsWith;
	}

	@Override
	public void permitAll() {
		permitAll = true;
	}

}
