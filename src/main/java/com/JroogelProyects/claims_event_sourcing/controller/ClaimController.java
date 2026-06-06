package com.JroogelProyects.claims_event_sourcing.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.JroogelProyects.claims_event_sourcing.dto.ClaimRequest;
import com.JroogelProyects.claims_event_sourcing.dto.ClaimResponse;
import com.JroogelProyects.claims_event_sourcing.service.ClaimCommandService;

@RestController
@RequestMapping("/api/claims")
public class ClaimController {
    
    private final ClaimCommandService service;

    public ClaimController(ClaimCommandService service){
        this.service = service;
    }

    @PostMapping
    ResponseEntity<ClaimResponse> postClaim(@RequestBody ClaimRequest request){
        ClaimResponse response = service.createClaim(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
