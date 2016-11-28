package io.skysail.server.app.pline.resources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.skysail.server.app.pline.Confirmation;
import io.skysail.server.app.pline.PlineApplication;
import io.skysail.server.app.pline.Registration;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class PostConfirmationResource extends PostEntityServerResource<Confirmation> {

    private PlineApplication app;

    @Override
    protected void doInit() {
        app = (PlineApplication) getApplication();
    }


    @Override
    public Confirmation createEntityTemplate() {
        return new Confirmation();
    }

    @Override
    public void addEntity(Confirmation entity) {

        Registration provider = app.getRepo().findOne(getAttribute("id"));

        //Filter filter = new Filter("(&(code="+entity.getCode()+")(email="+entity.getEmail().replace("@","&#64;")+"))");
        Map<String, Object> params = new HashMap<>();
        params.put("code", entity.getCode());
        params.put("email", entity.getEmail().replace("&#64;", "[at]"));
        String sql = "email=:email";
        List<Registration> followers = app.getRepo().find(Registration.class, sql, params);
        if (followers.size() == 1) {
            Registration follower = followers.get(0);
            provider.getFollowers().add(follower);
        }
        app.getRepo().save(provider, getApplicationModel());
    }

}
