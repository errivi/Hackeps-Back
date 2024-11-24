package com.apache.poi;

public class PersonTramitator {
    private String idTramitator, idSesion;
    private Tramit lastTramit;

    public PersonTramitator(String idTramitator, String idSesion) {
        this.idTramitator = idTramitator;
        this.idSesion = idSesion;
    }
    public Tramit getLastTramit() {
        return lastTramit;
    }

    public void setLastTramit(Tramit newTramit) {
        this.lastTramit = newTramit;
    }
}
