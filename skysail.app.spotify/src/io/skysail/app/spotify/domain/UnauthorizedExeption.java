package io.skysail.app.spotify.domain;

public class UnauthorizedExeption extends RuntimeException {

    private static final long serialVersionUID = -3540864858638299373L;

    public UnauthorizedExeption(String message) {
        super(message);
    }

}
