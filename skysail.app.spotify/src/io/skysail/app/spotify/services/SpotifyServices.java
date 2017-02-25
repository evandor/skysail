package io.skysail.app.spotify.services;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.skysail.app.spotify.domain.Callback;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SpotifyServices {
    
    private static ObjectMapper mapper = new ObjectMapper();
    
    @Getter
    private static Callback accessData;

    public static void setAccessData(String text) {
        try {
            SpotifyServices.accessData = mapper.readValue(text, Callback.class);
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
        
    }
}
