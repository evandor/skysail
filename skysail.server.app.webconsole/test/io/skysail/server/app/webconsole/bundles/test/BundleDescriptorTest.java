package io.skysail.server.app.webconsole.bundles.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.osgi.framework.Bundle;
import org.osgi.framework.Version;

import io.skysail.webconsole.osgi.entities.bundles.BundleDescriptor;

public class BundleDescriptorTest {

	private Bundle bundle;

	@Before
	public void setup() {
		bundle = Mockito.mock(Bundle.class);
		Mockito.when(bundle.getBundleId()).thenReturn(1L);
		Mockito.when(bundle.getVersion()).thenReturn(new Version("1.0.0"));
		Mockito.when(bundle.getSymbolicName()).thenReturn("my.bundle");
	}

	@Test
	public void properties_are_derived_from_bundle() {
		BundleDescriptor descriptor = new BundleDescriptor(bundle,null);
		assertThat(descriptor.getId(),is("1"));
		assertThat(descriptor.getVersion(),is("1.0.0"));
		assertThat(descriptor.getSymbolicName(),is("my.bundle"));
	}

	@Test
	public void states_are_derived_from_bundle() {
		Mockito.when(bundle.getState()).thenReturn(Bundle.ACTIVE);
		BundleDescriptor descriptor = new BundleDescriptor(bundle,null);
		assertThat(descriptor.getState(),is("ACTIVE"));

		Mockito.when(bundle.getState()).thenReturn(Bundle.INSTALLED);
		descriptor = new BundleDescriptor(bundle,null);
		assertThat(descriptor.getState(),is("INSTALLED"));

		Mockito.when(bundle.getState()).thenReturn(Bundle.RESOLVED);
		descriptor = new BundleDescriptor(bundle,null);
		assertThat(descriptor.getState(),is("RESOLVED"));

		Mockito.when(bundle.getState()).thenReturn(Bundle.STARTING);
		descriptor = new BundleDescriptor(bundle,null);
		assertThat(descriptor.getState(),is("STARTING"));

		Mockito.when(bundle.getState()).thenReturn(Bundle.STOPPING);
		descriptor = new BundleDescriptor(bundle,null);
		assertThat(descriptor.getState(),is("STOPPING"));

		Mockito.when(bundle.getState()).thenReturn(Bundle.UNINSTALLED);
		descriptor = new BundleDescriptor(bundle,null);
		assertThat(descriptor.getState(),is("UNINSTALLED"));
	}
}
