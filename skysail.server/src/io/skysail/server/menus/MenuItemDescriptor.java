package io.skysail.server.menus;

import io.skysail.domain.Entity;
import io.skysail.domain.html.Field;
import io.skysail.server.forms.ListView;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MenuItemDescriptor implements Entity {

    @Field
    @ListView
    private String url;

    private String name;

    public MenuItemDescriptor(MenuItem menuItem) {
        name = menuItem.getName();
        String link = menuItem.getLink();
        url = "<a href='"+link+"'>"+name+"</a>";
    }

    @Override
    public String getId() {
        return url;
    }


}
