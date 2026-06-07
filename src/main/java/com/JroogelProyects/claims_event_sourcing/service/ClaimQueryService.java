package com.JroogelProyects.claims_event_sourcing.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.JroogelProyects.claims_event_sourcing.domain.model.ClaimEntity;
import com.JroogelProyects.claims_event_sourcing.dto.ClaimResponse;
import com.JroogelProyects.claims_event_sourcing.repository.ClaimRepository;

@Service
public class ClaimQueryService {

    private final ClaimRepository repository;

    public ClaimQueryService(ClaimRepository repository) {
        this.repository = repository;
    }

    public ClaimResponse getClaimById(UUID id) {
        Optional<ClaimEntity> optionalEntity = repository.findById(id);
        if (optionalEntity.isPresent()) {
            ClaimEntity entity = optionalEntity.get();
            ClaimResponse response = ClaimResponse.builder()
                    .amount(entity.getAmount())
                    .createdAt(entity.getCreatedAt())
                    .description(entity.getDescription())
                    .id(entity.getClaimId())
                    .status(entity.getStatus())
                    .type(entity.getType())
                    .policyHolderId(entity.getPolicyHolderId())
                    .updatedAt(entity.getUpdatedAt())
                    .build();
            return response;
        }else{
            throw new RuntimeException("Claim not found" + id);
        }
    }

    public List<ClaimResponse> getAllClaims() {
        List<ClaimEntity> entities = repository.findAll();
        List<ClaimResponse> responses = new ArrayList<>();
        for (ClaimEntity entity : entities) {
            ClaimResponse response = ClaimResponse.builder()
                    .amount(entity.getAmount())
                    .createdAt(entity.getCreatedAt())
                    .description(entity.getDescription())
                    .id(entity.getClaimId())
                    .status(entity.getStatus())
                    .type(entity.getType())
                    .policyHolderId(entity.getPolicyHolderId())
                    .updatedAt(entity.getUpdatedAt())
                    .build();

            responses.add(response);
        }

        return responses;
    }
}
