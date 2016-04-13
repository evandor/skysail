package io.skysail.server.security.config;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class PathExpressionRegistry {

	@Getter
	private List<PathExpression> entries = new ArrayList<>();

	public PathExpression startsWithMatcher(String startsWith) {
		StartsWithPathExpression pathExpression = new StartsWithPathExpression(startsWith);
		entries.add(pathExpression);
		return pathExpression;
	}

}
