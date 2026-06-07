package com.JroogelProyects.claims_event_sourcing.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.JroogelProyects.claims_event_sourcing.domain.model.ClaimEvent;

public interface ClaimEventRepository extends JpaRepository<ClaimEvent, UUID>{
  
    public List<ClaimEvent> findByClaimIdOrderByCreatedAtAsc(UUID claimId);
}
