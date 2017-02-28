package io.skysail.app.spotify.domain;

public class TokenExpiredException extends Exception {

    private static final long serialVersionUID = -8131626586025782388L;

    public TokenExpiredException(String message) {
        super(message);
    }

}
