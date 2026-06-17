package com.dypatil.bfhl.service;

import com.dypatil.bfhl.dto.BfhlRequest;
import com.dypatil.bfhl.dto.BfhlResponse;

public interface BfhlService {
    BfhlResponse processRequest(BfhlRequest request, String requestId);
    BfhlResponse getAsyncResult(String correlationId);
}
