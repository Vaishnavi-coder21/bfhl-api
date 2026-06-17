package com.dypatil.bfhl.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BfhlResponse {
    @JsonProperty("is_success")
    private boolean isSuccess;

    @JsonProperty("request_id")
    private String requestId;

    @JsonProperty("odd_numbers")
    private List<String> oddNumbers;

    @JsonProperty("even_numbers")
    private List<String> evenNumbers;

    private List<String> alphabets;

    @JsonProperty("special_characters")
    private List<String> specialCharacters;

    private String sum;

    @JsonProperty("largest_number")
    private String largestNumber;

    @JsonProperty("smallest_number")
    private String smallestNumber;

    @JsonProperty("alphabet_count")
    private int alphabetCount;

    @JsonProperty("number_count")
    private int numberCount;

    @JsonProperty("special_character_count")
    private int specialCharacterCount;

    @JsonProperty("contains_duplicates")
    private boolean containsDuplicates;

    @JsonProperty("processing_time_ms")
    private long processingTimeMs;

    @JsonProperty("alphabet_frequency")
    private Map<String, Integer> alphabetFrequency;

    @JsonProperty("unique_element_count")
    private int uniqueElementCount;

    @JsonProperty("sorted_numbers")
    private List<String> sortedNumbers;

    @JsonProperty("vowel_count")
    private int vowelCount;

    @JsonProperty("consonant_count")
    private int consonantCount;

    @JsonProperty("longest_alphabetic_value")
    private String longestAlphabeticValue;

    @JsonProperty("shortest_alphabetic_value")
    private String shortestAlphabeticValue;

    private SummaryDto summary;

    // Async support fields
    @JsonProperty("correlation_id")
    private String correlationId;

    private String status;

    private String message;

    public BfhlResponse() {}

    public BfhlResponse(boolean isSuccess, String requestId, List<String> oddNumbers, List<String> evenNumbers,
                        List<String> alphabets, List<String> specialCharacters, String sum, String largestNumber,
                        String smallestNumber, int alphabetCount, int numberCount, int specialCharacterCount,
                        boolean containsDuplicates, long processingTimeMs, Map<String, Integer> alphabetFrequency,
                        int uniqueElementCount, List<String> sortedNumbers, int vowelCount, int consonantCount,
                        String longestAlphabeticValue, String shortestAlphabeticValue, SummaryDto summary,
                        String correlationId, String status, String message) {
        this.isSuccess = isSuccess;
        this.requestId = requestId;
        this.oddNumbers = oddNumbers;
        this.evenNumbers = evenNumbers;
        this.alphabets = alphabets;
        this.specialCharacters = specialCharacters;
        this.sum = sum;
        this.largestNumber = largestNumber;
        this.smallestNumber = smallestNumber;
        this.alphabetCount = alphabetCount;
        this.numberCount = numberCount;
        this.specialCharacterCount = specialCharacterCount;
        this.containsDuplicates = containsDuplicates;
        this.processingTimeMs = processingTimeMs;
        this.alphabetFrequency = alphabetFrequency;
        this.uniqueElementCount = uniqueElementCount;
        this.sortedNumbers = sortedNumbers;
        this.vowelCount = vowelCount;
        this.consonantCount = consonantCount;
        this.longestAlphabeticValue = longestAlphabeticValue;
        this.shortestAlphabeticValue = shortestAlphabeticValue;
        this.summary = summary;
        this.correlationId = correlationId;
        this.status = status;
        this.message = message;
    }

    public boolean isSuccess() { return isSuccess; }
    public void setSuccess(boolean success) { isSuccess = success; }

    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }

    public List<String> getOddNumbers() { return oddNumbers; }
    public void setOddNumbers(List<String> oddNumbers) { this.oddNumbers = oddNumbers; }

    public List<String> getEvenNumbers() { return evenNumbers; }
    public void setEvenNumbers(List<String> evenNumbers) { this.evenNumbers = evenNumbers; }

    public List<String> getAlphabets() { return alphabets; }
    public void setAlphabets(List<String> alphabets) { this.alphabets = alphabets; }

    public List<String> getSpecialCharacters() { return specialCharacters; }
    public void setSpecialCharacters(List<String> specialCharacters) { this.specialCharacters = specialCharacters; }

    public String getSum() { return sum; }
    public void setSum(String sum) { this.sum = sum; }

    public String getLargestNumber() { return largestNumber; }
    public void setLargestNumber(String largestNumber) { this.largestNumber = largestNumber; }

    public String getSmallestNumber() { return smallestNumber; }
    public void setSmallestNumber(String smallestNumber) { this.smallestNumber = smallestNumber; }

    public int getAlphabetCount() { return alphabetCount; }
    public void setAlphabetCount(int alphabetCount) { this.alphabetCount = alphabetCount; }

    public int getNumberCount() { return numberCount; }
    public void setNumberCount(int numberCount) { this.numberCount = numberCount; }

    public int getSpecialCharacterCount() { return specialCharacterCount; }
    public void setSpecialCharacterCount(int specialCharacterCount) { this.specialCharacterCount = specialCharacterCount; }

    public boolean isContainsDuplicates() { return containsDuplicates; }
    public void setContainsDuplicates(boolean containsDuplicates) { this.containsDuplicates = containsDuplicates; }

    public long getProcessingTimeMs() { return processingTimeMs; }
    public void setProcessingTimeMs(long processingTimeMs) { this.processingTimeMs = processingTimeMs; }

    public Map<String, Integer> getAlphabetFrequency() { return alphabetFrequency; }
    public void setAlphabetFrequency(Map<String, Integer> alphabetFrequency) { this.alphabetFrequency = alphabetFrequency; }

    public int getUniqueElementCount() { return uniqueElementCount; }
    public void setUniqueElementCount(int uniqueElementCount) { this.uniqueElementCount = uniqueElementCount; }

    public List<String> getSortedNumbers() { return sortedNumbers; }
    public void setSortedNumbers(List<String> sortedNumbers) { this.sortedNumbers = sortedNumbers; }

    public int getVowelCount() { return vowelCount; }
    public void setVowelCount(int vowelCount) { this.vowelCount = vowelCount; }

    public int getConsonantCount() { return consonantCount; }
    public void setConsonantCount(int consonantCount) { this.consonantCount = consonantCount; }

    public String getLongestAlphabeticValue() { return longestAlphabeticValue; }
    public void setLongestAlphabeticValue(String longestAlphabeticValue) { this.longestAlphabeticValue = longestAlphabeticValue; }

    public String getShortestAlphabeticValue() { return shortestAlphabeticValue; }
    public void setShortestAlphabeticValue(String shortestAlphabeticValue) { this.shortestAlphabeticValue = shortestAlphabeticValue; }

    public SummaryDto getSummary() { return summary; }
    public void setSummary(SummaryDto summary) { this.summary = summary; }

    public String getCorrelationId() { return correlationId; }
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public static BfhlResponseBuilder builder() {
        return new BfhlResponseBuilder();
    }

    public static class BfhlResponseBuilder {
        private boolean isSuccess;
        private String requestId;
        private List<String> oddNumbers;
        private List<String> evenNumbers;
        private List<String> alphabets;
        private List<String> specialCharacters;
        private String sum;
        private String largestNumber;
        private String smallestNumber;
        private int alphabetCount;
        private int numberCount;
        private int specialCharacterCount;
        private boolean containsDuplicates;
        private long processingTimeMs;
        private Map<String, Integer> alphabetFrequency;
        private int uniqueElementCount;
        private List<String> sortedNumbers;
        private int vowelCount;
        private int consonantCount;
        private String longestAlphabeticValue;
        private String shortestAlphabeticValue;
        private SummaryDto summary;
        private String correlationId;
        private String status;
        private String message;

        public BfhlResponseBuilder isSuccess(boolean isSuccess) { this.isSuccess = isSuccess; return this; }
        public BfhlResponseBuilder requestId(String requestId) { this.requestId = requestId; return this; }
        public BfhlResponseBuilder oddNumbers(List<String> oddNumbers) { this.oddNumbers = oddNumbers; return this; }
        public BfhlResponseBuilder evenNumbers(List<String> evenNumbers) { this.evenNumbers = evenNumbers; return this; }
        public BfhlResponseBuilder alphabets(List<String> alphabets) { this.alphabets = alphabets; return this; }
        public BfhlResponseBuilder specialCharacters(List<String> specialCharacters) { this.specialCharacters = specialCharacters; return this; }
        public BfhlResponseBuilder sum(String sum) { this.sum = sum; return this; }
        public BfhlResponseBuilder largestNumber(String largestNumber) { this.largestNumber = largestNumber; return this; }
        public BfhlResponseBuilder smallestNumber(String smallestNumber) { this.smallestNumber = smallestNumber; return this; }
        public BfhlResponseBuilder alphabetCount(int alphabetCount) { this.alphabetCount = alphabetCount; return this; }
        public BfhlResponseBuilder numberCount(int numberCount) { this.numberCount = numberCount; return this; }
        public BfhlResponseBuilder specialCharacterCount(int specialCharacterCount) { this.specialCharacterCount = specialCharacterCount; return this; }
        public BfhlResponseBuilder containsDuplicates(boolean containsDuplicates) { this.containsDuplicates = containsDuplicates; return this; }
        public BfhlResponseBuilder processingTimeMs(long processingTimeMs) { this.processingTimeMs = processingTimeMs; return this; }
        public BfhlResponseBuilder alphabetFrequency(Map<String, Integer> alphabetFrequency) { this.alphabetFrequency = alphabetFrequency; return this; }
        public BfhlResponseBuilder uniqueElementCount(int uniqueElementCount) { this.uniqueElementCount = uniqueElementCount; return this; }
        public BfhlResponseBuilder sortedNumbers(List<String> sortedNumbers) { this.sortedNumbers = sortedNumbers; return this; }
        public BfhlResponseBuilder vowelCount(int vowelCount) { this.vowelCount = vowelCount; return this; }
        public BfhlResponseBuilder consonantCount(int consonantCount) { this.consonantCount = consonantCount; return this; }
        public BfhlResponseBuilder longestAlphabeticValue(String longestAlphabeticValue) { this.longestAlphabeticValue = longestAlphabeticValue; return this; }
        public BfhlResponseBuilder shortestAlphabeticValue(String shortestAlphabeticValue) { this.shortestAlphabeticValue = shortestAlphabeticValue; return this; }
        public BfhlResponseBuilder summary(SummaryDto summary) { this.summary = summary; return this; }
        public BfhlResponseBuilder correlationId(String correlationId) { this.correlationId = correlationId; return this; }
        public BfhlResponseBuilder status(String status) { this.status = status; return this; }
        public BfhlResponseBuilder message(String message) { this.message = message; return this; }

        public BfhlResponse build() {
            return new BfhlResponse(isSuccess, requestId, oddNumbers, evenNumbers, alphabets, specialCharacters,
                    sum, largestNumber, smallestNumber, alphabetCount, numberCount, specialCharacterCount,
                    containsDuplicates, processingTimeMs, alphabetFrequency, uniqueElementCount, sortedNumbers,
                    vowelCount, consonantCount, longestAlphabeticValue, shortestAlphabeticValue, summary,
                    correlationId, status, message);
        }
    }
}
