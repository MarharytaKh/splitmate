package com.splitmate.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class GroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private long postAndGetId(String url, String json) throws Exception {
        String body = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(body);
        return node.get("id").asLong();
    }

    @Test
    void createGroupAndGetIt() throws Exception {
        long id = postAndGetId("/api/groups", "{\"name\": \"Trip to Gdansk\"}");

        mockMvc.perform(get("/api/groups/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Trip to Gdansk"))
                .andExpect(jsonPath("$.members").isEmpty());
    }

    @Test
    void emptyNameReturns400() throws Exception {
        mockMvc.perform(post("/api/groups").contentType(MediaType.APPLICATION_JSON).content("{\"name\": \"  \"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("group name can't be empty"));
    }

    @Test
    void unknownGroupReturns404() throws Exception {
        mockMvc.perform(get("/api/groups/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void fullFlowWithSettleUp() throws Exception {
        long groupId = postAndGetId("/api/groups", "{\"name\": \"Flat\"}");
        long ola = postAndGetId("/api/groups/" + groupId + "/members", "{\"name\": \"Ola\", \"avatar\": 6}");
        long kuba = postAndGetId("/api/groups/" + groupId + "/members", "{\"name\": \"Kuba\", \"avatar\": 1}");

        String expense = """
                {"description": "groceries", "amountCents": 5000, "paidById": %d, "participantIds": [%d, %d]}
                """.formatted(ola, ola, kuba);
        postAndGetId("/api/groups/" + groupId + "/expenses", expense);

        mockMvc.perform(get("/api/groups/" + groupId + "/balances"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].balanceCents").value(2500))
                .andExpect(jsonPath("$[1].balanceCents").value(-2500));

        mockMvc.perform(get("/api/groups/" + groupId + "/settle-up"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].from.name").value("Kuba"))
                .andExpect(jsonPath("$[0].to.name").value("Ola"))
                .andExpect(jsonPath("$[0].amountCents").value(2500));
    }

    @Test
    void cantAddSameNameTwice() throws Exception {
        long groupId = postAndGetId("/api/groups", "{\"name\": \"Dorm\"}");
        postAndGetId("/api/groups/" + groupId + "/members", "{\"name\": \"Ola\", \"avatar\": 2}");

        mockMvc.perform(post("/api/groups/" + groupId + "/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"ola\", \"avatar\": 3}"))
                .andExpect(status().isBadRequest());
    }
}
