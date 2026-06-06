package com.JroogelProyects.claims_event_sourcing.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.JroogelProyects.claims_event_sourcing.domain.model.ClaimEntity;

@Repository
public interface ClaimRepository extends JpaRepository<ClaimEntity, UUID>{

}
