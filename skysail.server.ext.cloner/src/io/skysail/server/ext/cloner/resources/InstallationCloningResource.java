package io.skysail.server.ext.cloner.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

public class InstallationCloningResource extends EntityServerResource<Empty>{

    @Override
    public Empty getEntity() {
        String filename = "bnd.bnd";
//        FileInputStream fis = new FileInputStream(filename);
//
//        int b = 0;
//        while ((b = fis.read()) != -1) {
////                response.getOutputStream().write(b);
//        }
        return null;
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        return new SkysailResponse<>();
    }


}
