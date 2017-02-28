package io.skysail.app.spotify.config;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.metatype.annotations.Designate;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL, configurationPid = "spotify", service = SpotifyConfiguration.class)
@Designate(ocd = SpotifyConfigDescriptor.class)
public class SpotifyConfiguration {

    @Getter
    private SpotifyConfigDescriptor config;

    @Activate
    public void activate(SpotifyConfigDescriptor config) {
        this.config = config;
    }

    @Deactivate
    protected void deactivate(ComponentContext ctxt) {
        config = null;
    }

}