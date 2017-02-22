package io.skysail.server.codegen.model;

import io.skysail.domain.core.ApplicationModel;

public class JavaApplication extends ApplicationModel implements JavaModel {

    private String packageName;

    private String basePackage;

    public JavaApplication(String id) {
        super(id.substring(id.lastIndexOf(".")+1));
        this.packageName = "pkgName";//getPackageFromName(id);
        this.basePackage = id.substring(0,id.lastIndexOf("."));
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

    public String getBasePackage() {
        return basePackage;
    }

}
