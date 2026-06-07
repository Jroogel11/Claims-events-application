package com.JroogelProyects.claims_event_sourcing.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.JroogelProyects.claims_event_sourcing.domain.enums.ClaimEventType;
import com.JroogelProyects.claims_event_sourcing.domain.enums.ClaimStatus;
import com.JroogelProyects.claims_event_sourcing.domain.enums.ClaimType;
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

    public ClaimResponse updateClaim(String type, String id){
        Optional<ClaimEntity> entity = repository.findById(UUID.fromString(id));
        if ( entity.isPresent() ){
            ClaimResponse response = ClaimResponse.builder()
            .amount(entity.get().getAmount())
            .createdAt(entity.get().getCreatedAt())
            .description(entity.get().getDescription())
            .policyHolderId(entity.get().getPolicyHolderId())
            .type(entity.get().getType())
            .status(entity.get().getStatus())
            .updatedAt(entity.get().getUpdatedAt())
            .build();

            ClaimEventMessage message = ClaimEventMessage.builder()
            .claimId(entity.get().getClaimId().toString())
            .occurredAt(LocalDateTime.now().toString())
            .policyHolderId(entity.get().getPolicyHolderId())
            .type(ClaimEventType.valueOf(type))
            .eventId(UUID.randomUUID().toString())
            .build();

            producer.sendClaimEvent(message);

            return response;
        }else{
            throw new RuntimeException("Claim not found" + id);
        }
    }

}
