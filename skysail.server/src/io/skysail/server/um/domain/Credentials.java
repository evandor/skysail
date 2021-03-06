package io.skysail.server.um.domain;

import javax.validation.constraints.Size;

import io.skysail.domain.Entity;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.InputType;

public class Credentials implements Entity {

    @Size(min = 3, message = "Username must have at least three characters")
    @Field
    private String username;

    @Size(min = 3, message = "Password must have at least three characters")
    @Field(inputType = InputType.PASSWORD)
    private String password;

    public Credentials() {
    }

    public Credentials(String username, String password) {
        this.username = username.replace("@", "&#64;");
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getId() {
        return null;
    }

}
