package io.skysail.app.spotify.config;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;

import io.skysail.ext.oauth2.config.OAuth2ConfigDescriptor;
import lombok.Getter;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL, configurationPid = "spotify", service = SpotifyConfiguration.class)
public class SpotifyConfiguration {

    @Getter
    private OAuth2ConfigDescriptor config;

    @Activate
    public void activate(OAuth2ConfigDescriptor config) {
        this.config = config;
    }

    @Deactivate
    protected void deactivate(ComponentContext ctxt) {
        config = null;
    }

}