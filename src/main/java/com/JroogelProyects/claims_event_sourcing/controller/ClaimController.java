package com.JroogelProyects.claims_event_sourcing.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.JroogelProyects.claims_event_sourcing.domain.model.ClaimEvent;
import com.JroogelProyects.claims_event_sourcing.dto.ClaimRequest;
import com.JroogelProyects.claims_event_sourcing.dto.ClaimResponse;
import com.JroogelProyects.claims_event_sourcing.dto.UpdateClaim;
import com.JroogelProyects.claims_event_sourcing.service.ClaimCommandService;
import com.JroogelProyects.claims_event_sourcing.service.ClaimQueryService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/claims")
public class ClaimController {

    private final ClaimCommandService service;
    private final ClaimQueryService getService;

    public ClaimController(ClaimCommandService service, ClaimQueryService getService) {
        this.service = service;
        this.getService = getService;
    }

    @PostMapping
    ResponseEntity<ClaimResponse> postClaim(@RequestBody ClaimRequest request) {
        ClaimResponse response = service.createClaim(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<ClaimResponse> getClaimByID(@PathVariable String id) {
        return ResponseEntity.ok(getService.getClaimById(UUID.fromString(id)));
    }

    @GetMapping
    public ResponseEntity<List<ClaimResponse>> getAllClaims() {
        return ResponseEntity.ok(getService.getAllClaims());
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<ClaimEvent>> getAllEvents(@PathVariable String id) {
        return ResponseEntity.ok(getService.getEventsByID(UUID.fromString(id)));
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<ClaimResponse> updateClaim(@RequestBody UpdateClaim type, @PathVariable String id){
        return ResponseEntity.ok(service.updateClaim(type.getType().toString(), id));
    }
    
    

}
