package io.skysail.server.forms;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessagesUtils {

    public static String getBaseKey(Class<? extends Object> entityClass, FormField f) {
    	return f.getId();
    }

    public static String getSimpleName(FormField f) {
        return f.getId();
    }

}
