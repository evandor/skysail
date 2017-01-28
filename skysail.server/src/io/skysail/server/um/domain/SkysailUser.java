package io.skysail.server.um.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Id;

import io.skysail.domain.Entity;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.InputType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SkysailUser implements Entity {

    /**
     * this username is reserved; altering any data should be prevented by the
     * system.
     */
    public static final String SYSTEM_USER = "system";

    @Id
    private String id;

    @Field
    private String username;

    @Field(inputType = InputType.PASSWORD)
    private String password;

    @Field(inputType = InputType.EMAIL)
    private String email;

    @Field
    private String name;

    @Field
    private String surname;

    private Set<SkysailRole> roles = new HashSet<>();

    public SkysailUser(String username, String password, String id) {
        this.username = username;
        this.password = password;
        this.id = id;
    }

    @Override
    public String toString() {
        return username + "[" + roles.toString() + "]";
    }

}
