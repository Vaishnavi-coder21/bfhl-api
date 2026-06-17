package com.dypatil.bfhl.controller;

import com.dypatil.bfhl.dto.BfhlRequest;
import com.dypatil.bfhl.dto.BfhlResponse;
import com.dypatil.bfhl.service.BfhlService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping
@CrossOrigin(origins = "*")
@Validated
public class BfhlController {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BfhlController.class);

    private final BfhlService bfhlService;

    public BfhlController(BfhlService bfhlService) {
        this.bfhlService = bfhlService;
    }

    @PostMapping("/bfhl")
    public ResponseEntity<BfhlResponse> processData(
            @Valid @RequestBody BfhlRequest request,
            @RequestHeader(value = "X-Request-Id", required = false) String requestId) {
        
        if (requestId == null || requestId.trim().isEmpty()) {
            requestId = "REQ-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            log.info("X-Request-Id header not found. Generated: {}", requestId);
        }

        log.info("Received POST /bfhl request. Request ID: {}, Payload size: {}", 
                requestId, request.getData() != null ? request.getData().size() : 0);
        
        BfhlResponse response = bfhlService.processRequest(request, requestId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/bfhl")
    public ResponseEntity<?> handleGetBfhl(
            @RequestParam(value = "correlation_id", required = false) String correlationId) {
        
        if (correlationId != null && !correlationId.trim().isEmpty()) {
            log.info("Received GET /bfhl for correlationId: {}", correlationId);
            BfhlResponse response = bfhlService.getAsyncResult(correlationId);
            if ("NOT_FOUND".equals(response.getStatus())) {
                return ResponseEntity.status(404).body(response);
            }
            return ResponseEntity.ok(response);
        }

        log.info("Received GET /bfhl (operation code query)");
        Map<String, Object> response = new HashMap<>();
        response.put("operation_code", 1);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> getHealth() {
        log.info("Received GET /health request");
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", System.currentTimeMillis());
        health.put("service", "D.Y.Patil Campus Hiring API");
        return ResponseEntity.ok(health);
    }
}
