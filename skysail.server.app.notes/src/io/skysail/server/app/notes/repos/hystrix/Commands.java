package io.skysail.server.app.notes.repos.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class Commands  extends HystrixCommand<String> {

	private String name;

	public Commands(String name) {
        super(HystrixCommandGroupKey.Factory.asKey("AWS"));
        this.name = name;
    }

	@Override
	protected String run() throws Exception {
		return "Hello " + name + "!";
	}

}
