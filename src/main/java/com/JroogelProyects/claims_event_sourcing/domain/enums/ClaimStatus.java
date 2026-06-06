package com.JroogelProyects.claims_event_sourcing.domain.enums;

public enum ClaimStatus {
    DECLARED("Claim reported with expedient opened"),
    UNDER_EVALUATION("Under evaluation by professionals"),
    UNDER_REPAIR("The damaged property is being repaired in a assigned center"),
    UNDER_DOCUMENTATION("The claim is waiting for the documentation"),
    RESOLVED("The payment for the claim has been done"),
    CLOSED("The claim has been concluded correctly"),
    REJECTED("The company has denied the claim");

    private final String description;

    ClaimStatus(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }
}
