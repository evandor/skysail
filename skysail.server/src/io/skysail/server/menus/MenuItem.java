package io.skysail.server.menus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Predicate;

import io.skysail.domain.Entity;
import io.skysail.domain.html.Field;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(of = {"category","name","parent"})
@ToString(of = {"name", "link", "category"})
public class MenuItem implements Entity, Comparable<MenuItem> {

    public enum Category {
        APPLICATION_MAIN_MENU, //
        ADMIN_MENU, //
        ADMIN_MAIN_MENU_INTERACTIVITY, // Section in Admin Main Menu
        FRONTENDS_MAIN_MENU,
        DESIGNER_APP_MENU
    }

    @Setter
    private String id;
    
    @Field
    private String name;

    @Field
    private String link;
    
    @Field
    private Category category = Category.ADMIN_MENU;

    @Field
    private MenuItem parent;

    private List<MenuItem> children = new ArrayList<>();

    @Field
    private Predicate<String[]> securedByRole;
    
    @Field
    private boolean needsAuthentication = true;
    
    private boolean openInNewWindow = false;
	private String applicationImage = "";

	@Setter
    private String icon;

    public MenuItem(MenuItem parent, String name, String link) {
        this.parent = parent;
        this.id = name;
        if (parent != null) {
            this.id = parent.getId() + "/" + name;
            parent.addMenuItem(this);
        }

        this.name = name;
        this.link = link;
    }

    public MenuItem(String name, String link) {
        this(null, name, link);
    }

    public void setCategory(MenuItem.Category category) {
        this.category = category;
    }

    protected void addMenuItem(MenuItem menuItem) {
        this.children.add(menuItem);
    }

    public List<MenuItem> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public void setOpenInNewWindow(boolean openInNewWindow) {
        this.openInNewWindow = openInNewWindow;
    }

    public boolean isOpenInNewWindow() {
        return openInNewWindow;
    }

    public Predicate<String[]> getSecuredByRole() {
        return securedByRole;
    }

    public boolean getNeedsAuthentication() {
        return needsAuthentication;
    }

    public boolean isAdminMainMenuInteractivity () {
        return category.equals(Category.ADMIN_MAIN_MENU_INTERACTIVITY);
    }

    public String getApplicationImage() {
	    return applicationImage;
    }

	@Override
	public int compareTo(MenuItem o) {
		return this.getName().compareTo(o.getName());
	}

}
