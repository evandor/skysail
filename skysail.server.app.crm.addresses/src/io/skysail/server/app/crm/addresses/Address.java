package io.skysail.server.app.crm.addresses;

import javax.persistence.Id;

import io.skysail.domain.Entity;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.InputType;
import io.skysail.server.codegen.ResourceType;
import io.skysail.server.codegen.annotations.GenerateResources;
import io.skysail.server.polymer.elements.GoogleMap;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@GenerateResources(application = "io.skysail.server.app.crm.addresses.AddressesApplication", exclude = {
		ResourceType.POST })
public class Address implements Entity {

	@Id
	private String id;

	@Field // (onEvent = {"focus: geolocate()"})
	private String combined;

	// @Field
	private String street_number, street, city, state, zip, country; // NOSONAR

	@Field // (inputType = InputType.READONLY)
	private String apiKey;

	// @Field(inputType = InputType.POLYMER, fieldAttributes = {"apiKey"})
	// private GoogleMapsPlacesAutocomplete autocomplete;

	@Field // (inputType = InputType.READONLY)
	private String latitude = "37.77493", longitude = "-122.41942";

	@Field(inputType = InputType.POLYMER, fieldAttributes = { "apiKey", "latitude", "longitude" })
	private GoogleMap googleMapExample;

}