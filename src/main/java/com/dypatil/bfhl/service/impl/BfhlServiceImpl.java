package com.dypatil.bfhl.service.impl;

import com.dypatil.bfhl.dto.BfhlRequest;
import com.dypatil.bfhl.dto.BfhlResponse;
import com.dypatil.bfhl.dto.SummaryDto;
import com.dypatil.bfhl.service.BfhlService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BfhlServiceImpl implements BfhlService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BfhlServiceImpl.class);

    private final Map<String, BfhlResponse> asyncResults = new ConcurrentHashMap<>();
    private final Map<String, String> asyncStatus = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @Override
    public BfhlResponse processRequest(BfhlRequest request, String requestId) {
        long startTime = System.currentTimeMillis();
        List<String> rawData = request.getData();
        int totalElementsReceived = rawData != null ? rawData.size() : 0;

        if (totalElementsReceived > 10000) {
            log.info("Large payload detected: {} elements. Processing asynchronously.", totalElementsReceived);
            String correlationId = UUID.randomUUID().toString();
            asyncStatus.put(correlationId, "PROCESSING");

            List<String> rawDataCopy = rawData != null ? new ArrayList<>(rawData) : Collections.emptyList();

            executorService.submit(() -> {
                try {
                    BfhlResponse response = processDataSync(rawDataCopy, requestId, startTime);
                    response.setCorrelationId(correlationId);
                    response.setStatus("COMPLETED");
                    asyncResults.put(correlationId, response);
                    asyncStatus.put(correlationId, "COMPLETED");
                    log.info("Async processing completed for correlationId: {}", correlationId);
                } catch (Exception e) {
                    log.error("Error processing async request for correlationId: {}", correlationId, e);
                    asyncStatus.put(correlationId, "FAILED: " + e.getMessage());
                }
            });

            return BfhlResponse.builder()
                    .isSuccess(true)
                    .requestId(requestId)
                    .correlationId(correlationId)
                    .status("PROCESSING")
                    .message("Large payload (>10,000 records) detected. Processing asynchronously.")
                    .build();
        }

        return processDataSync(rawData, requestId, startTime);
    }

    @Override
    public BfhlResponse getAsyncResult(String correlationId) {
        String status = asyncStatus.get(correlationId);
        if (status == null) {
            return BfhlResponse.builder()
                    .isSuccess(false)
                    .status("NOT_FOUND")
                    .message("No request found for correlation ID: " + correlationId)
                    .build();
        }

        if ("COMPLETED".equals(status)) {
            return asyncResults.get(correlationId);
        }

        if (status.startsWith("FAILED")) {
            return BfhlResponse.builder()
                    .isSuccess(false)
                    .status("FAILED")
                    .message("Async processing failed: " + status.substring(8))
                    .build();
        }

        return BfhlResponse.builder()
                .isSuccess(true)
                .correlationId(correlationId)
                .status(status)
                .message("Request is still processing.")
                .build();
    }

    private BfhlResponse processDataSync(List<String> rawData, String requestId, long startTime) {
        if (rawData == null || rawData.isEmpty()) {
            long duration = System.currentTimeMillis() - startTime;
            return BfhlResponse.builder()
                    .isSuccess(true)
                    .requestId(requestId)
                    .oddNumbers(Collections.emptyList())
                    .evenNumbers(Collections.emptyList())
                    .alphabets(Collections.emptyList())
                    .specialCharacters(Collections.emptyList())
                    .sum("0")
                    .largestNumber("")
                    .smallestNumber("")
                    .alphabetCount(0)
                    .numberCount(0)
                    .specialCharacterCount(0)
                    .containsDuplicates(false)
                    .processingTimeMs(duration)
                    .alphabetFrequency(Collections.emptyMap())
                    .uniqueElementCount(0)
                    .sortedNumbers(Collections.emptyList())
                    .vowelCount(0)
                    .consonantCount(0)
                    .longestAlphabeticValue("")
                    .shortestAlphabeticValue("")
                    .summary(new SummaryDto(0, 0, 0))
                    .build();
        }

        // 1. Calculate Summary (raw filtering status)
        int totalElementsReceived = rawData.size();
        List<String> validElements = new ArrayList<>();
        int invalidElementsIgnored = 0;

        for (String element : rawData) {
            if (element == null || element.isEmpty() || element.trim().isEmpty()) {
                invalidElementsIgnored++;
            } else {
                validElements.add(element);
            }
        }
        int validElementsProcessed = validElements.size();
        SummaryDto summary = new SummaryDto(totalElementsReceived, validElementsProcessed, invalidElementsIgnored);

        // 2. Duplicate Check and Removal
        Set<String> seen = new HashSet<>();
        List<String> uniqueInputs = new ArrayList<>();
        boolean containsDuplicates = false;

        for (String element : validElements) {
            if (!seen.add(element)) {
                containsDuplicates = true;
            } else {
                uniqueInputs.add(element);
            }
        }
        int uniqueElementCount = uniqueInputs.size();

        // 3. Category Lists
        List<BigDecimal> allNumbers = new ArrayList<>();
        List<BigDecimal> oddNumbers = new ArrayList<>();
        List<BigDecimal> evenNumbers = new ArrayList<>();
        List<String> alphabets = new ArrayList<>();
        List<String> specialCharacters = new ArrayList<>();

        // 4. Processing unique elements
        for (String element : uniqueInputs) {
            if (isNumeric(element)) {
                BigDecimal num = new BigDecimal(element);
                allNumbers.add(num);
                categorizeOddEven(num, oddNumbers, evenNumbers);
            } else if (isAlphabetic(element)) {
                alphabets.add(element.toUpperCase());
            } else if (isAlphanumeric(element)) {
                // Per spec: for alphanumeric strings, extract alphabets ONLY.
                // Numbers embedded in alphanumeric strings are NOT processed as numeric values.
                List<String> alphas = extractAlphabets(element);
                alphabets.addAll(alphas);
            } else {
                specialCharacters.add(element);
            }
        }

        // 5. Compute numbers statistics
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal num : allNumbers) {
            sum = sum.add(num);
        }
        String sumStr = formatBigDecimal(sum);

        String largestNumberStr = "";
        String smallestNumberStr = "";
        if (!allNumbers.isEmpty()) {
            BigDecimal largest = allNumbers.stream().max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
            BigDecimal smallest = allNumbers.stream().min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
            largestNumberStr = formatBigDecimal(largest);
            smallestNumberStr = formatBigDecimal(smallest);
        }

        // Sort number lists
        List<String> oddNumbersSorted = oddNumbers.stream()
                .sorted()
                .map(BfhlServiceImpl::formatBigDecimal)
                .toList();

        List<String> evenNumbersSorted = evenNumbers.stream()
                .sorted()
                .map(BfhlServiceImpl::formatBigDecimal)
                .toList();

        List<String> sortedNumbers = allNumbers.stream()
                .sorted()
                .map(BfhlServiceImpl::formatBigDecimal)
                .toList();

        // 6. Compute alphabets statistics
        Map<String, Integer> alphabetFrequency = new TreeMap<>();
        int vowelCount = 0;
        int consonantCount = 0;

        for (String alpha : alphabets) {
            for (char c : alpha.toCharArray()) {
                if (Character.isLetter(c)) {
                    String charStr = String.valueOf(c).toUpperCase();
                    alphabetFrequency.put(charStr, alphabetFrequency.getOrDefault(charStr, 0) + 1);
                    if (isVowel(c)) {
                        vowelCount++;
                    } else {
                        consonantCount++;
                    }
                }
            }
        }

        // Longest and shortest pure alphabetic strings received in raw input
        List<String> pureAlphabetsFromInput = validElements.stream()
                .filter(this::isAlphabetic)
                .toList();

        String longestAlphabeticValue = "";
        String shortestAlphabeticValue = "";
        if (!pureAlphabetsFromInput.isEmpty()) {
            longestAlphabeticValue = pureAlphabetsFromInput.stream()
                    .max(Comparator.comparingInt(String::length))
                    .map(String::toUpperCase)
                    .orElse("");
            shortestAlphabeticValue = pureAlphabetsFromInput.stream()
                    .min(Comparator.comparingInt(String::length))
                    .map(String::toUpperCase)
                    .orElse("");
        }

        long duration = System.currentTimeMillis() - startTime;

        return BfhlResponse.builder()
                .isSuccess(true)
                .requestId(requestId)
                .oddNumbers(oddNumbersSorted)
                .evenNumbers(evenNumbersSorted)
                .alphabets(alphabets)
                .specialCharacters(specialCharacters)
                .sum(sumStr)
                .largestNumber(largestNumberStr)
                .smallestNumber(smallestNumberStr)
                .alphabetCount(alphabets.size())
                .numberCount(allNumbers.size())
                .specialCharacterCount(specialCharacters.size())
                .containsDuplicates(containsDuplicates)
                .processingTimeMs(duration)
                .alphabetFrequency(alphabetFrequency)
                .uniqueElementCount(uniqueElementCount)
                .sortedNumbers(sortedNumbers)
                .vowelCount(vowelCount)
                .consonantCount(consonantCount)
                .longestAlphabeticValue(longestAlphabeticValue)
                .shortestAlphabeticValue(shortestAlphabeticValue)
                .summary(summary)
                .build();
    }

    private boolean isNumeric(String str) {
        if (str == null) return false;
        try {
            new BigDecimal(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isAlphabetic(String str) {
        return str != null && str.matches("^[a-zA-Z]+$");
    }

    private boolean isAlphanumeric(String str) {
        if (str == null) return false;
        boolean hasLetter = false;
        boolean hasDigit = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isLetter(c)) {
                hasLetter = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else {
                return false; // contains special char
            }
        }
        return hasLetter && hasDigit;
    }

    private List<String> extractNumbers(String str) {
        List<String> numbers = new ArrayList<>();
        Matcher matcher = Pattern.compile("-?\\d+(\\.\\d+)?").matcher(str);
        while (matcher.find()) {
            numbers.add(matcher.group());
        }
        return numbers;
    }

    private List<String> extractAlphabets(String str) {
        List<String> alphabets = new ArrayList<>();
        for (char c : str.toCharArray()) {
            if (Character.isLetter(c)) {
                alphabets.add(String.valueOf(c).toUpperCase());
            }
        }
        return alphabets;
    }

    private void categorizeOddEven(BigDecimal num, List<BigDecimal> oddNumbers, List<BigDecimal> evenNumbers) {
        BigDecimal stripped = num.stripTrailingZeros();
        if (stripped.scale() <= 0) {
            try {
                BigInteger bi = stripped.toBigInteger();
                if (bi.remainder(BigInteger.valueOf(2)).abs().equals(BigInteger.ZERO)) {
                    evenNumbers.add(num);
                } else {
                    oddNumbers.add(num);
                }
            } catch (Exception e) {
                log.warn("Failed to check odd/even for number: {}", num, e);
            }
        }
    }

    private boolean isVowel(char c) {
        char uc = Character.toUpperCase(c);
        return uc == 'A' || uc == 'E' || uc == 'I' || uc == 'O' || uc == 'U';
    }

    public static String formatBigDecimal(BigDecimal bd) {
        if (bd == null) return "0";
        BigDecimal stripped = bd.stripTrailingZeros();
        if (stripped.scale() <= 0) {
            return stripped.toBigInteger().toString();
        }
        return stripped.toPlainString();
    }
}
