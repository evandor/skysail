package io.skysail.server.app.pline.resources;

import io.skysail.domain.Identifiable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Mail implements Identifiable {

    private String id;

    private String from = "evandor@gmail.com";
    private String to = "mira.v.graef@gmail.com";
    private String subject = "subject";
    private String content = "hi";
}
