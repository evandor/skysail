package io.skysail.server.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityContextHolder {

	private static final ThreadLocal<SecurityContext> contextHolder = new ThreadLocal<>();

	public static void clearContext() {
		contextHolder.remove();
	}

	public static SecurityContext getContext() {
		SecurityContext ctx = contextHolder.get();

		if (ctx == null) {
			ctx = createEmptyContext();
			contextHolder.set(ctx);
		}

		return ctx;
	}
	
	public static void setContext(@NonNull SecurityContext context) {
		contextHolder.set(context);
	}

	public static SecurityContext createEmptyContext() {
		return new SecurityContext();
	}

}
