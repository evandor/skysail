package io.skysail.server.db.test.entities.folders;

import java.util.*;

import io.skysail.domain.Entity;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Folder implements Entity {

    private String id;

    private String name;

    private List<Folder> subfolder = new ArrayList<>();

    public Folder(String name) {
        this.name = name;
    }

}
