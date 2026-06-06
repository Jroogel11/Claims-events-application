package com.JroogelProyects.claims_event_sourcing.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.JroogelProyects.claims_event_sourcing.domain.enums.ClaimStatus;
import com.JroogelProyects.claims_event_sourcing.domain.enums.ClaimType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClaimResponse {
    
    private UUID id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String policyHolderId;
    private BigDecimal amount;
    private String description;
    private ClaimStatus status;
    private ClaimType type;
}
