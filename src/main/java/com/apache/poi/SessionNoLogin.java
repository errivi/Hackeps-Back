package com.apache.poi;

public class SessionNoLogin {
    private String idSesion;
    private Tramit lastTramit;

    public SessionNoLogin(String idSesion) {
        this.idSesion = idSesion;
    }

    public Tramit getLastTramit() {
        return lastTramit;
    }

    public void setLastTramit(Tramit newTramit) {
        this.lastTramit = newTramit;
    }
}
