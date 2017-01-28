package io.skysail.server.app.demo;

import io.skysail.domain.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Mail implements Entity {

    private String id;

    private String from = "evandor@gmail.com";
    private String to = "mira.v.graef@gmail.com";
    private String subject = "subject";
    private String content = "hi";
}
