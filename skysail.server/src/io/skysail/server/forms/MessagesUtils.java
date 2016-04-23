package io.skysail.server.forms;

public class MessagesUtils {

    public static String getBaseKey(Class<? extends Object> entityClass, FormField f) {
        if (entityClass == null) {
            return "unnamedEntity";
        }
        if (entityClass.getName().contains("_$$_")) {
            entityClass = entityClass.getSuperclass();
        }
        return entityClass.getName() + "." + f.getId();
    }

    public static String getSimpleName(FormField f) {
        return f.getId();
    }

}
