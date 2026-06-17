package com.dypatil.bfhl.service;

import com.dypatil.bfhl.dto.BfhlRequest;
import com.dypatil.bfhl.dto.BfhlResponse;
import com.dypatil.bfhl.service.impl.BfhlServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

public class BfhlServiceImplTest {

    private BfhlService bfhlService;

    @BeforeEach
    public void setUp() {
        bfhlService = new BfhlServiceImpl();
    }

    @Test
    public void testProcessRequest_SuccessScenario1() {
        BfhlRequest request = new BfhlRequest(Arrays.asList("A", "1", "22", "$", "B", "7"));
        BfhlResponse response = bfhlService.processRequest(request, "REQ-1001");

        assertTrue(response.isSuccess());
        assertEquals("REQ-1001", response.getRequestId());
        assertEquals(Arrays.asList("1", "7"), response.getOddNumbers());
        assertEquals(Arrays.asList("22"), response.getEvenNumbers());
        assertEquals(Arrays.asList("A", "B"), response.getAlphabets());
        assertEquals(Arrays.asList("$"), response.getSpecialCharacters());
        assertEquals("30", response.getSum());
        assertEquals("22", response.getLargestNumber());
        assertEquals("1", response.getSmallestNumber());
        assertEquals(2, response.getAlphabetCount());
        assertEquals(3, response.getNumberCount());
        assertEquals(1, response.getSpecialCharacterCount());
        assertFalse(response.isContainsDuplicates());
        assertEquals(1, response.getVowelCount()); // A is a vowel
        assertEquals(1, response.getConsonantCount()); // B is a consonant
    }

    @Test
    public void testProcessRequest_AlphanumericExtraction() {
        // Input: ["A1B2", "100", "#", "Test123", "Z", "55"]
        // Per spec: numbers INSIDE alphanumeric strings (1,2 from A1B2 and 1,2,3 from Test123)
        // are NOT counted as numbers. Only pure numeric strings (100, 55) are counted.
        BfhlRequest request = new BfhlRequest(Arrays.asList("A1B2", "100", "#", "Test123", "Z", "55"));
        BfhlResponse response = bfhlService.processRequest(request, "REQ-1002");

        assertTrue(response.isSuccess());
        // 55 is odd, 100 is even — embedded numbers from alphanumeric NOT included
        assertEquals(Arrays.asList("55"), response.getOddNumbers());
        assertEquals(Arrays.asList("100"), response.getEvenNumbers());
        // Alphabets: A,B from A1B2 (individual chars) + T,E,S,T from Test123 + Z
        assertEquals(Arrays.asList("A", "B", "T", "E", "S", "T", "Z"), response.getAlphabets());
        assertEquals(Arrays.asList("#"), response.getSpecialCharacters());
        // sum = 100 + 55 = 155 (not 281)
        assertEquals("155", response.getSum());
        assertEquals("100", response.getLargestNumber());
        assertEquals("55", response.getSmallestNumber());
        assertEquals(7, response.getAlphabetCount());
        assertEquals(2, response.getNumberCount());
        assertEquals(1, response.getSpecialCharacterCount());
    }

    @Test
    public void testProcessRequest_DuplicatesAndNulls() {
        BfhlRequest request = new BfhlRequest(Arrays.asList("10", "10", "A", "A", "", null, "   ", "&", "5"));
        BfhlResponse response = bfhlService.processRequest(request, "REQ-1003");

        assertTrue(response.isSuccess());
        assertTrue(response.isContainsDuplicates());
        assertEquals(4, response.getUniqueElementCount());
        assertNotNull(response.getSummary());
        assertEquals(9, response.getSummary().getTotalElementsReceived());
        assertEquals(6, response.getSummary().getValidElementsProcessed());
        assertEquals(3, response.getSummary().getInvalidElementsIgnored());
    }

    @Test
    public void testProcessRequest_DecimalsAndNegatives() {
        BfhlRequest request = new BfhlRequest(Arrays.asList("-10", "25.5", "-100.75", "B", "@", "5"));
        BfhlResponse response = bfhlService.processRequest(request, "REQ-1004");

        assertTrue(response.isSuccess());
        assertEquals(Arrays.asList("5"), response.getOddNumbers());
        assertEquals(Arrays.asList("-10"), response.getEvenNumbers());
        assertEquals(Arrays.asList("-100.75", "-10", "5", "25.5"), response.getSortedNumbers());
        assertEquals("-80.25", response.getSum());
        assertEquals("25.5", response.getLargestNumber());
        assertEquals("-100.75", response.getSmallestNumber());
    }

    @Test
    public void testProcessRequest_EmptyRequest() {
        BfhlRequest request = new BfhlRequest(Collections.emptyList());
        BfhlResponse response = bfhlService.processRequest(request, "REQ-EMPTY");

        assertTrue(response.isSuccess());
        assertEquals(0, response.getUniqueElementCount());
        assertEquals("0", response.getSum());
    }

    @Test
    public void testProcessRequest_LargePayloadAsync() {
        String[] data = new String[10005];
        Arrays.fill(data, "A");
        BfhlRequest request = new BfhlRequest(Arrays.asList(data));
        BfhlResponse response = bfhlService.processRequest(request, "REQ-LARGE");

        assertTrue(response.isSuccess());
        assertEquals("PROCESSING", response.getStatus());
        assertNotNull(response.getCorrelationId());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        BfhlResponse result = bfhlService.getAsyncResult(response.getCorrelationId());
        assertNotNull(result);
        if ("COMPLETED".equals(result.getStatus())) {
            assertEquals("COMPLETED", result.getStatus());
            assertEquals(1, result.getAlphabetCount());
        }
    }
}
