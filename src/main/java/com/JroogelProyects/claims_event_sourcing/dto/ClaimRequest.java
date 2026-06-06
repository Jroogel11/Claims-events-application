package com.JroogelProyects.claims_event_sourcing.dto;

import java.math.BigDecimal;

import com.JroogelProyects.claims_event_sourcing.domain.enums.ClaimType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClaimRequest {
    
    private String policyOrderId;

    private String description;

    private BigDecimal amount;

    private ClaimType type;

}
