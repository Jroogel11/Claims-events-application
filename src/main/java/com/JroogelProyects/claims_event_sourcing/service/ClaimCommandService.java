package com.JroogelProyects.claims_event_sourcing.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.JroogelProyects.claims_event_sourcing.domain.enums.ClaimEventType;
import com.JroogelProyects.claims_event_sourcing.domain.enums.ClaimStatus;
import com.JroogelProyects.claims_event_sourcing.domain.model.ClaimEntity;
import com.JroogelProyects.claims_event_sourcing.dto.ClaimEventMessage;
import com.JroogelProyects.claims_event_sourcing.dto.ClaimRequest;
import com.JroogelProyects.claims_event_sourcing.dto.ClaimResponse;
import com.JroogelProyects.claims_event_sourcing.kafka.ClaimEventProducer;
import com.JroogelProyects.claims_event_sourcing.repository.ClaimRepository;

@Service
public class ClaimCommandService {
    
    private final ClaimRepository repository;
    private final ClaimEventProducer producer;

    public ClaimCommandService(ClaimRepository repository, ClaimEventProducer producer){
        this.repository = repository;
        this.producer = producer;
    }

    public ClaimResponse createClaim(ClaimRequest request){
        ClaimEntity entity = ClaimEntity.builder()
        .amount(request.getAmount())
        .createdAt(LocalDateTime.now())
        .description(request.getDescription())
        .status(ClaimStatus.DECLARED)
        .type(request.getType())
        .updatedAt(null)
        .policyHolderId(request.getPolicyOrderId())
        .build();
        repository.save(entity);

        ClaimEventMessage message = ClaimEventMessage.builder()
        .eventId(UUID.randomUUID().toString())
        .claimId(entity.getClaimId().toString())
        .type(ClaimEventType.CLAIM_DECLARED)
        .policyHolderId(entity.getPolicyHolderId())
        .occurredAt(entity.getCreatedAt().toString())
        .build();

        producer.sendClaimEvent(message);

        ClaimResponse response = ClaimResponse.builder()
        .amount(entity.getAmount())
        .createdAt(entity.getCreatedAt())
        .description(entity.getDescription())
        .id(entity.getClaimId())
        .policyHolderId(entity.getPolicyHolderId())
        .status(entity.getStatus())
        .type(entity.getType())
        .updatedAt(entity.getUpdatedAt())
        .build();

        return response;

    }

}
