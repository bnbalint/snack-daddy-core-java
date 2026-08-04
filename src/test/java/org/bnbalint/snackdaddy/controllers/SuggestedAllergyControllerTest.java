package org.bnbalint.snackdaddy.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bnbalint.snackdaddy.models.SuggestedAllergy;
import org.bnbalint.snackdaddy.repositories.SuggestedAllergyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SuggestedAllergyController.class)
public class SuggestedAllergyControllerTest {

    static Instant DATE = Instant.parse("2026-07-01T00:00:01Z");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // Spring automatically provides this instance


    @MockBean
    private SuggestedAllergyRepository suggestedAllergyRepo;


    //---------------------------------------------------------------
    // getAllSuggestedAllergies
    //

    @Test
    void test_getAllSuggestedAllergies_success() throws Exception {
        //--------------------------------------------------
        // SET VALUES
        SuggestedAllergy suggestedAllergy = new SuggestedAllergy("Pine nut");
        suggestedAllergy.setId(1);

        //--------------------------------------------------
        // CONFIGURE MOCKS
        when(suggestedAllergyRepo.findAll()).thenReturn(List.of(suggestedAllergy));

        //--------------------------------------------------
        // EXECUTE & VERIFY RESULTS
        mockMvc.perform(get("/suggested-allergies")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print()) // print the response
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].name").value("Pine nut"));
    }


    @Test
    void test_getAllSuggestedAllergies_error() throws Exception {
        //--------------------------------------------------
        // SET VALUES

        //--------------------------------------------------
        // CONFIGURE MOCKS
        when(suggestedAllergyRepo.findAll()).thenThrow(new IllegalArgumentException("DB error"));

        //--------------------------------------------------
        // EXECUTE & VERIFY RESULTS
        mockMvc.perform(get("/suggested-allergies")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andDo(print()); // print the response
    }

    //---------------------------------------------------------------
    // addSuggestedAllergy
    //
    @Test
    void test_addSuggestedAllergy_success() throws Exception {
        //--------------------------------------------------
        // SET VALUES

        // create the one to send in the request
        SuggestedAllergy suggestedAllergy = new SuggestedAllergy("Pine nut");

        // create the one to return from the mock database
        SuggestedAllergy savedSuggestion = new SuggestedAllergy("Pine nut");
        savedSuggestion.setId(1);
        savedSuggestion.setCreatedAt(DATE);
        savedSuggestion.setUpdatedAt(DATE);

        //--------------------------------------------------
        // CONFIGURE MOCKS
        when(suggestedAllergyRepo.save(any())).thenReturn(savedSuggestion);

        //--------------------------------------------------
        // EXECUTE & VERIFY RESULTS
        mockMvc.perform(post("/suggested-allergies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(suggestedAllergy))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString())) // print the response
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Pine nut"))
                .andExpect(jsonPath("$.created_at").value(DATE.atOffset(ZoneOffset.UTC).toString()))
                .andExpect(jsonPath("$.updated_at").value(DATE.atOffset(ZoneOffset.UTC).toString()));
    }

    @Test
    void test_addSuggestedAllergy_conflict() throws Exception {
        //--------------------------------------------------
        // SET VALUES
        SuggestedAllergy suggestedAllergy = new SuggestedAllergy("Pine nut");

        //--------------------------------------------------
        // CONFIGURE MOCKS
        when(suggestedAllergyRepo.save(any())).thenThrow(new OptimisticLockingFailureException("DB conflict"));

        //--------------------------------------------------
        // EXECUTE & VERIFY RESULTS
        mockMvc.perform(post("/suggested-allergies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(suggestedAllergy))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString())); // print the response
    }

    @Test
    void test_addSuggestedAllergy_error() throws Exception {
        //--------------------------------------------------
        // SET VALUES
        SuggestedAllergy suggestedAllergy = new SuggestedAllergy("Pine nut");

        //--------------------------------------------------
        // CONFIGURE MOCKS
        when(suggestedAllergyRepo.save(any())).thenThrow(new IllegalArgumentException("DB error"));

        //--------------------------------------------------
        // EXECUTE & VERIFY RESULTS
        mockMvc.perform(post("/suggested-allergies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(suggestedAllergy))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString())); // print the response
    }
}
