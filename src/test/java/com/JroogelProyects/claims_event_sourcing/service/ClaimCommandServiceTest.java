package com.JroogelProyects.claims_event_sourcing.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.JroogelProyects.claims_event_sourcing.domain.enums.ClaimStatus;
import com.JroogelProyects.claims_event_sourcing.domain.enums.ClaimType;
import com.JroogelProyects.claims_event_sourcing.domain.model.ClaimEntity;
import com.JroogelProyects.claims_event_sourcing.dto.ClaimEventMessage;
import com.JroogelProyects.claims_event_sourcing.dto.ClaimRequest;
import com.JroogelProyects.claims_event_sourcing.dto.ClaimResponse;
import com.JroogelProyects.claims_event_sourcing.kafka.ClaimEventProducer;
import com.JroogelProyects.claims_event_sourcing.repository.ClaimRepository;

@ExtendWith(MockitoExtension.class)
public class ClaimCommandServiceTest {

    @Mock
    private ClaimRepository repository;

    @Mock
    private ClaimEventProducer producer;

    @InjectMocks
    private ClaimCommandService service;

    @Test
    void createClaim_shouldReturnDeclaredStatus() {
        ClaimRequest request = ClaimRequest.builder()
                .amount(BigDecimal.valueOf(1000))
                .description("Description of test Claim")
                .policyOrderId("user-123")
                .type(ClaimType.LIFE)
                .build();

        when(repository.save(any(ClaimEntity.class))).thenAnswer(invocation -> {
            ClaimEntity entity = invocation.getArgument(0);
            entity.setClaimId(UUID.randomUUID());
            return entity;
        });

        ClaimResponse response = service.createClaim(request);

        assertEquals(ClaimStatus.DECLARED, response.getStatus());
        verify(producer, times(1)).sendClaimEvent(any(ClaimEventMessage.class));
    }

    @Test
    void updateClaim_withInvalidTransition_shouldThrowException() {
        UUID id = UUID.randomUUID();
        ClaimEntity entity = ClaimEntity.builder()
                .claimId(id)
                .status(ClaimStatus.DECLARED)
                .type(ClaimType.LIFE)
                .policyHolderId("user-123")
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(entity));

        assertThrows(RuntimeException.class, () -> service.updateClaim("CLAIM_CLOSED", id.toString()));
    }

    @Test
    void updateClaim_withNonExistentClaim_shouldThrowException() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.updateClaim("EVALUATION_STARTED", id.toString()));
    }
}