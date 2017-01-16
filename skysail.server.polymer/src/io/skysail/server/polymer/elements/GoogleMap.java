package io.skysail.server.polymer.elements;

import io.skysail.server.services.PolymerElementDefinition;
import lombok.Setter;

public class GoogleMap implements PolymerElementDefinition {
    
    @Setter
    private String apiKey;

    @Setter
    private String latitude;

    @Setter
    private String longitude;

    @Override
    public String render() {
        return "<style>google-map { height: 200px; } </style><google-map latitude='"+latitude+"' longitude='"+longitude+"' api-key='"+apiKey+"'></google-map>";
    }

}
