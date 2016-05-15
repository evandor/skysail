package io.skysail.server.app;

public @interface ApplicationConfiguration {

	long DEFAULT_MAX_AGE = 1800;

	/**
	 * List of allowed origins, e.g. {@code "http://mydomain.io"}.
	 *
	 * <p>These values are placed in the {@code Access-Control-Allow-Origin}
	 * header of both the pre-flight response and the actual response.
	 *
	 * <p>{@code "*"} means that all origins are allowed; if empty (the default),
	 * no CORS headers will be set at all.
	 */
	String[] corsOrigins() default {};

	/**
	 * List of request headers that can be used during the actual request.
	 *
	 * <p>This property controls the value of the pre-flight response's
	 * {@code Access-Control-Allow-Headers} header.
	 *
	 * <p>{@code "*"} means that all headers requested by the client are allowed; if empty (the default),
	 * all request headers are allowed.
	 */
	String[] corsAllowedHeaders() default {"*"};

	/**
	 * List of response headers that the user-agent will allow the client to access.
	 *
	 * <p>This property controls the value of actual response's
	 * {@code Access-Control-Expose-Headers} header.
	 *
	 * <p>If undefined, the exposed header list will be empty..
	 */
	String[] corsExposedHeaders() default {};

	/**
	 * List of supported HTTP request methods, e.g. GET, POST, ...
	 */
	String[] corsMethods() default {};

	/**
	 * Whether the browser should include any cookies associated with the
	 * domain of the request being annotated.
     *
	 * <p>Default: {@code "false"}, i.e. such cookies should not included.
	 *
	 * {@code "true"} means that the pre-flight response will include the header
	 * {@code Access-Control-Allow-Credentials=true}.
	 */
	String corsAllowCredentials() default "false";

	/**
	 * The maximum age (in seconds) of the cache duration for pre-flight responses.
	 *
	 * <p>This property controls the value of the {@code Access-Control-Max-Age}
	 * header in the pre-flight response.
	 *
	 */
	// Not used yet
	//long corsMaxAge() default -1; // NOSONAR
}
