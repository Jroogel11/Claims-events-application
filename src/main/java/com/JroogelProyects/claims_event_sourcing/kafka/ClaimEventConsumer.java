package com.JroogelProyects.claims_event_sourcing.kafka;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.JroogelProyects.claims_event_sourcing.domain.enums.ClaimEventType;
import com.JroogelProyects.claims_event_sourcing.domain.enums.ClaimStatus;
import com.JroogelProyects.claims_event_sourcing.domain.model.ClaimEntity;
import com.JroogelProyects.claims_event_sourcing.domain.model.ClaimEvent;
import com.JroogelProyects.claims_event_sourcing.dto.ClaimEventMessage;
import com.JroogelProyects.claims_event_sourcing.repository.ClaimEventRepository;
import com.JroogelProyects.claims_event_sourcing.repository.ClaimRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ClaimEventConsumer {

    private final ClaimRepository repository;
    private final ClaimEventRepository eventRepository;

    public ClaimEventConsumer(ClaimRepository repository, ClaimEventRepository eventRepository) {
        this.repository = repository;
        this.eventRepository = eventRepository;
    }

    @KafkaListener(topics = "${kafka.topics.claim-events}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(ClaimEventMessage message) {
        Optional<ClaimEntity> entity = repository.findById(UUID.fromString(message.getClaimId()));
        if (entity.isPresent()) {
            ClaimStatus status = switch (message.getType()) {
                case CLAIM_CLOSED -> ClaimStatus.CLOSED;
                case CLAIM_DECLARED -> ClaimStatus.DECLARED;
                case CLAIM_REJECTED -> ClaimStatus.REJECTED;
                case CLAIM_RESOLVED -> ClaimStatus.RESOLVED;
                case DOCUMENTATION_REQUESTED -> ClaimStatus.UNDER_DOCUMENTATION;
                case EVALUATION_STARTED -> ClaimStatus.UNDER_EVALUATION;
                case REPAIR_STARTED -> ClaimStatus.UNDER_REPAIR;
            };
            entity.get().setStatus(status);
            repository.save(entity.get());
            log.info("The entity {} has been saved", entity.get());

            ClaimEvent event = ClaimEvent.builder()
                    .claimId(UUID.fromString(message.getClaimId()))
                    .createdAt(LocalDateTime.now())
                    .policyHolderId(message.getPolicyHolderId())
                    .type(message.getType())
                    .build();

            eventRepository.save(event);
        } else {
            log.error("Error {} is not in the repository", message.getClaimId());
        }

    }
}
