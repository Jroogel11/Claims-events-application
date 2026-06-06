package com.JroogelProyects.claims_event_sourcing.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


import com.JroogelProyects.claims_event_sourcing.domain.enums.ClaimStatus;
import com.JroogelProyects.claims_event_sourcing.domain.enums.ClaimType;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "claims")
public class ClaimEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID claimId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String policyHolderId;

    @Column(nullable = false)
    private String description;

    @Column
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ClaimStatus status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ClaimType type;

}
