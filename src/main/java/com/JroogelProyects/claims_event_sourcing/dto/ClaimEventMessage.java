package com.JroogelProyects.claims_event_sourcing.dto;

import com.JroogelProyects.claims_event_sourcing.domain.enums.ClaimEventType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClaimEventMessage {
    
    private String eventId;
    private String claimId;
    private ClaimEventType type;
    private String policyHolderId;
    private String occurredAt;

}
