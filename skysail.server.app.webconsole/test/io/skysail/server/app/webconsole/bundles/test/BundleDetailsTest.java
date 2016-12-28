package io.skysail.server.app.webconsole.bundles.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.osgi.framework.Bundle;
import org.osgi.framework.Version;

import io.skysail.webconsole.osgi.entities.bundles.BundleDetails;
@Ignore
public class BundleDetailsTest {

	private Bundle bundle;

	private Map<String,String> headersMap;

	@Before
	public void setup() {
		bundle = Mockito.mock(Bundle.class);
		Mockito.when(bundle.getBundleId()).thenReturn(1L);
		Mockito.when(bundle.getVersion()).thenReturn(new Version("1.0.0"));
		Mockito.when(bundle.getSymbolicName()).thenReturn("my.bundle");

		headersMap = new HashMap<>();
		Mockito.when(bundle.getHeaders(null)).thenReturn(new Hashtable<>(headersMap));
	}

	@Test
	public void properties_are_derived_from_bundle() {
		BundleDetails details = new BundleDetails(bundle,null);
		assertThat(details.getId(),is("1"));
		assertThat(details.getVersion(),is("1.0.0"));
		assertThat(details.getSymbolicName(),is("my.bundle"));
	}

	@Test
	public void location_is_derived_from_bundle() {
		Mockito.when(bundle.getLocation()).thenReturn("theLocation");
		BundleDetails details = new BundleDetails(bundle,null);
		assertThat(details.getLocation(), is("theLocation"));
	}

	@Test
	public void lastModified_is_derived_from_bundle() {
		Date now = new Date();
		Mockito.when(bundle.getLastModified()).thenReturn(now.getTime());
		BundleDetails details = new BundleDetails(bundle,null);
		assertThat(details.getLastModification(), is(now.getTime()));
	}

	@Test
	public void docUrl_is_derived_from_bundle() {
		headersMap.put("Bundle-DocURL", "theUrl");
		Mockito.when(bundle.getHeaders(null)).thenReturn(new Hashtable<>(headersMap));
		BundleDetails details = new BundleDetails(bundle,null);
		assertThat(details.getDocUrl(), is("theUrl"));
	}

	@Test
	public void bundleVendor_is_derived_from_bundle() {
		headersMap.put("Bundle-Vendor", "vendor");
		Mockito.when(bundle.getHeaders(null)).thenReturn(new Hashtable<>(headersMap));
		BundleDetails details = new BundleDetails(bundle,null);
		assertThat(details.getVendor(), is("vendor"));
	}

	@Test
	public void bundleCopyright_is_derived_from_bundle() {
		headersMap.put("Bundle-Copyright", "copyright");
		Mockito.when(bundle.getHeaders(null)).thenReturn(new Hashtable<>(headersMap));
		BundleDetails details = new BundleDetails(bundle,null);
		assertThat(details.getCopyright(), is("copyright"));
	}

	@Test
	public void bundleDescription_is_derived_from_bundle() {
		headersMap.put("Bundle-Description", "desc");
		Mockito.when(bundle.getHeaders(null)).thenReturn(new Hashtable<>(headersMap));
		BundleDetails details = new BundleDetails(bundle,null);
		assertThat(details.getDescription(), is("desc"));
	}

	@Test
	public void bundleClasspath_is_derived_from_bundle() {
		headersMap.put("Bundle-ClassPath", "clsPath");
		Mockito.when(bundle.getHeaders(null)).thenReturn(new Hashtable<>(headersMap));
		BundleDetails details = new BundleDetails(bundle,null);
		assertThat(details.getBundleClasspath(), is("clsPath"));
	}

}
