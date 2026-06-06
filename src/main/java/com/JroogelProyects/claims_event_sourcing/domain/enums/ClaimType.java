package com.JroogelProyects.claims_event_sourcing.domain.enums;

public enum ClaimType {
    VEHICLE("Vehicle claims"),
    HOME("All variety of home claims"),
    HEALTH("Personal accidents related with health"),
    LIFE("Life claims"),
    TRAVEL("Travel claims"),
    LIABILITY("Civil responsability");

     private final String description;

    ClaimType(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }
}
