package io.skysail.server.ext.aws;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.metatype.annotations.Designate;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL, configurationPid = "aws", service = AwsConfiguration.class)
@Designate(ocd = AwsConfigDescriptor.class)
@Slf4j
public class AwsConfiguration {

	@Getter
	private AwsConfigDescriptor config;

	@Activate
	public void activate(AwsConfigDescriptor config) {
		log.debug("Activating {}", this.getClass().getName());
		this.config = config;
	}

	@Deactivate
	protected void deactivate(ComponentContext ctxt) {
		log.debug("Deactivating {}", this.getClass().getName());
		config = null;
	}

}