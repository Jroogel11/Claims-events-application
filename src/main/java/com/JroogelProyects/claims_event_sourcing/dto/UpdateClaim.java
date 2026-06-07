package com.JroogelProyects.claims_event_sourcing.dto;

import com.JroogelProyects.claims_event_sourcing.domain.enums.ClaimEventType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateClaim {
    private ClaimEventType type;
}
