package io.skysail.server.codegen.model;

import io.skysail.domain.core.ApplicationModel;

public class JavaApplication extends ApplicationModel implements JavaModel {

    private String packageName;

    public JavaApplication(String id) {
        super(id.substring(id.lastIndexOf(".")+1));
        this.packageName = "pkgName";//getPackageFromName(id);
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

}
