package com.dypatil.bfhl;

import com.dypatil.bfhl.dto.BfhlRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BfhlControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testPostBfhl_Success() throws Exception {
        BfhlRequest request = new BfhlRequest(Arrays.asList("A", "1", "22", "$", "B", "7"));

        mockMvc.perform(post("/bfhl")
                .header("X-Request-Id", "REQ-IT-1001")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.is_success", is(true)))
                .andExpect(jsonPath("$.request_id", is("REQ-IT-1001")))
                .andExpect(jsonPath("$.odd_numbers", contains("1", "7")))
                .andExpect(jsonPath("$.even_numbers", contains("22")))
                .andExpect(jsonPath("$.alphabets", contains("A", "B")))
                .andExpect(jsonPath("$.special_characters", contains("$")))
                .andExpect(jsonPath("$.sum", is("30")))
                .andExpect(jsonPath("$.largest_number", is("22")))
                .andExpect(jsonPath("$.smallest_number", is("1")))
                .andExpect(jsonPath("$.alphabet_count", is(2)))
                .andExpect(jsonPath("$.number_count", is(3)))
                .andExpect(jsonPath("$.special_character_count", is(1)))
                .andExpect(jsonPath("$.contains_duplicates", is(false)))
                .andExpect(jsonPath("$.vowel_count", is(1)))
                .andExpect(jsonPath("$.consonant_count", is(1)));
    }

    @Test
    public void testPostBfhl_ValidationFailed() throws Exception {
        BfhlRequest request = new BfhlRequest(null);

        mockMvc.perform(post("/bfhl")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.is_success", is(false)))
                .andExpect(jsonPath("$.error", containsString("Validation failed")));
    }

    @Test
    public void testGetBfhl_OperationCode() throws Exception {
        mockMvc.perform(get("/bfhl"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation_code", is(1)));
    }

    @Test
    public void testGetHealth_Up() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("UP")))
                .andExpect(jsonPath("$.service", is("D.Y.Patil Campus Hiring API")));
    }
}
