package com.JroogelProyects.claims_event_sourcing.domain.enums;

public enum ClaimEventType {
    CLAIM_DECLARED("A new claim was opened"),
    EVALUATION_STARTED("Claim entered evaluation phase"),
    REPAIR_STARTED("Damaged property sent to repair center"),
    DOCUMENTATION_REQUESTED("Additional documentation required"),
    CLAIM_RESOLVED("Claim approved and payment processed"),
    CLAIM_CLOSED("Claim administratively closed"),
    CLAIM_REJECTED("Claim denied by the insurer");

    private String description;

    ClaimEventType(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }
}
