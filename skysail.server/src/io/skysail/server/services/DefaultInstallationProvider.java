package io.skysail.server.services;

public class DefaultInstallationProvider implements InstallationProvider {

    @Override
    public int getPort() {
        return 0;
    }

    @Override
    public String getProductName() {
        return "skysail";
    }

}
