package io.skysail.server.polymer.elements;

import io.skysail.server.services.PolymerElementDefinition;
import lombok.Setter;

public class GoogleMapsPlacesAutocomplete extends PolymerElementDefinition {

    @Setter
    private String apiKey;

    @Override
    public String render() {
        return "<sky-google-maps-places-autocomplete api-key='"+apiKey+"'></sky-google-maps-places-autocomplete>";
    }

}
