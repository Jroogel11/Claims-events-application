package com.JroogelProyects.claims_event_sourcing.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.JroogelProyects.claims_event_sourcing.domain.enums.ClaimEventType;

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

@Entity
@Table(name = "events")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClaimEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID eventId;

    @Column(nullable = false)
    private UUID claimId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ClaimEventType type;

    @Column(nullable = false)
    private String policyHolderId;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}

