package org.bnbalint.snackdaddy.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bnbalint.snackdaddy.models.*;
import org.bnbalint.snackdaddy.repositories.SnackLogRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SnackLogController.class)
public class SnackLogControllerTest {

    static Instant DATE = Instant.parse("2026-07-01T00:00:01Z");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // Spring automatically provides this instance


    @MockBean
    private SnackLogRepository snackLogRepo;


    //---------------------------------------------------------------
    // getAllSnackLogEntries
    //

    @Test
    void test_getAllSnackLogEntries_success() throws Exception {
        //--------------------------------------------------
        // SET VALUES
        Ingredient ingredient1 = makeIngredient("Rice Crispy Cereal", 4);
        Ingredient ingredient2 = makeIngredient("Margarine", 5);
        Ingredient ingredient3 = makeIngredient("Marshmallow", 6);
        Ingredient ingredient4 = makeIngredient("Vanilla", 7);
        Ingredient[] ingredients = { ingredient1, ingredient2, ingredient3, ingredient4 };
        Snack snack = new Snack(
                "Rice Crispie Treat",
                true,
                false,
                2,
                ingredients
        );
        snack.setId(1);
        snack.setCreatedAt(DATE);
        snack.setUpdatedAt(DATE);

        Team team = new Team(
                "Mules",
                Rink.BAIREL,
                Level.D5,
                "#b88907",
                "#000000",
                "#c42323",
                "logo.com"
        );
        team.setId(1);
        team.setCreatedAt(DATE);
        team.setUpdatedAt(DATE);

        SnackLog snackLogEntry = new SnackLog(snack, team, LocalDate.of(2026, Month.JUNE, 1));
        snackLogEntry.setId(1);
        snackLogEntry.setCreatedAt(DATE);
        snackLogEntry.setUpdatedAt(DATE);
        System.out.println("SnackLogEntry = " + snackLogEntry);
        
        //--------------------------------------------------
        // CONFIGURE MOCKS
        when(snackLogRepo.findAll()).thenReturn(List.of(snackLogEntry));

        //--------------------------------------------------
        // EXECUTE & VERIFY RESULTS
        mockMvc.perform(get("/snack-log")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print()) // print the response

        .andExpect(jsonPath("$.[0].id").value(1))
        .andExpect(jsonPath("$.[0].snack.name").value("Rice Crispie Treat"))
        .andExpect(jsonPath("$.[0].snack.sweet").value(true))
        .andExpect(jsonPath("$.[0].snack.savory").value(false))
        .andExpect(jsonPath("$.[0].snack.difficulty").value(2))
        .andExpect(jsonPath("$.[0].snack.created_at").value(DATE.atOffset(ZoneOffset.UTC).toString()))
        .andExpect(jsonPath("$.[0].snack.updated_at").value(DATE.atOffset(ZoneOffset.UTC).toString()))
        .andExpect(jsonPath("$.[0].snack.ingredients").isArray())

        .andExpect(jsonPath("$.[0].team.name").value("Mules"))
        .andExpect(jsonPath("$.[0].team.rink").value("BAIREL"))
        .andExpect(jsonPath("$.[0].team.level").value("D5"))
        .andExpect(jsonPath("$.[0].team.primary_color").value("#b88907"))
        .andExpect(jsonPath("$.[0].team.secondary_color").value("#000000"))
        .andExpect(jsonPath("$.[0].team.ternary_color").value("#c42323"))
        .andExpect(jsonPath("$.[0].team.logo_url").value("logo.com"))

        .andExpect(jsonPath("$.[0].date_made").value("2026-06-01"))
        .andExpect(jsonPath("$.[0].created_at").value(DATE.atOffset(ZoneOffset.UTC).toString()))
        .andExpect(jsonPath("$.[0].updated_at").value(DATE.atOffset(ZoneOffset.UTC).toString()));
    }


    @Test
    void test_getAllSnackLogEntries_error() throws Exception {
        //--------------------------------------------------
        // SET VALUES


        //--------------------------------------------------
        // CONFIGURE MOCKS
        when(snackLogRepo.findAll()).thenThrow(new IllegalArgumentException("DB error"));

        //--------------------------------------------------
        // EXECUTE & VERIFY RESULTS
        mockMvc.perform(get("/snack-log")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andDo(print()); // print the response
    }

    //---------------------------------------------------------------
    // addToSnackLog
    //
    @Test
    void test_addToSnackLog_success() throws Exception {
        //--------------------------------------------------
        // SET VALUES
        Ingredient ingredient1 = makeIngredient("Rice Crispy Cereal", 4);
        Ingredient ingredient2 = makeIngredient("Margarine", 5);
        Ingredient ingredient3 = makeIngredient("Marshmallow", 6);
        Ingredient ingredient4 = makeIngredient("Vanilla", 7);
        Ingredient[] ingredients = { ingredient1, ingredient2, ingredient3, ingredient4 };
        Snack snack = new Snack(
                "Rice Crispie Treat",
                true,
                false,
                2,
                ingredients
        );
        snack.setId(1);
        snack.setCreatedAt(DATE);
        snack.setUpdatedAt(DATE);

        Team team = new Team(
                "Mules",
                Rink.BAIREL,
                Level.D5,
                "#b88907",
                "#000000",
                "#c42323",
                "logo.com"
        );
        team.setId(1);
        team.setCreatedAt(DATE);
        team.setUpdatedAt(DATE);

        // create the one to send in the request
        SnackLog snackLogEntry = new SnackLog(snack, team, LocalDate.of(2026, Month.JUNE, 1));

        // create the one to return from the mock database
        SnackLog savedSnackLogEntry = new SnackLog(snack, team, LocalDate.of(2026, Month.JUNE, 1));
        savedSnackLogEntry.setId(1);
        savedSnackLogEntry.setCreatedAt(DATE);
        savedSnackLogEntry.setUpdatedAt(DATE);

        //--------------------------------------------------
        // CONFIGURE MOCKS
        when(snackLogRepo.save(any())).thenReturn(savedSnackLogEntry);

        //--------------------------------------------------
        // EXECUTE & VERIFY RESULTS
        mockMvc.perform(post("/snack-log")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(snackLogEntry))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString())) // print the response
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.snack.name").value("Rice Crispie Treat"))
                .andExpect(jsonPath("$.snack.sweet").value(true))
                .andExpect(jsonPath("$.snack.savory").value(false))
                .andExpect(jsonPath("$.snack.difficulty").value(2))
                .andExpect(jsonPath("$.snack.created_at").value(DATE.atOffset(ZoneOffset.UTC).toString()))
                .andExpect(jsonPath("$.snack.updated_at").value(DATE.atOffset(ZoneOffset.UTC).toString()))
                .andExpect(jsonPath("$.snack.ingredients").isArray())

                .andExpect(jsonPath("$.team.name").value("Mules"))
                .andExpect(jsonPath("$.team.rink").value("BAIREL"))
                .andExpect(jsonPath("$.team.level").value("D5"))
                .andExpect(jsonPath("$.team.primary_color").value("#b88907"))
                .andExpect(jsonPath("$.team.secondary_color").value("#000000"))
                .andExpect(jsonPath("$.team.ternary_color").value("#c42323"))
                .andExpect(jsonPath("$.team.logo_url").value("logo.com"))

                .andExpect(jsonPath("$.date_made").value("2026-06-01"))
                .andExpect(jsonPath("$.created_at").value(DATE.atOffset(ZoneOffset.UTC).toString()))
                .andExpect(jsonPath("$.updated_at").value(DATE.atOffset(ZoneOffset.UTC).toString()));
    }

    @Test
    void test_addToSnackLog_conflict() throws Exception {
        //--------------------------------------------------
        // SET VALUES
        Ingredient ingredient1 = makeIngredient("Rice Crispy Cereal", 4);
        Ingredient ingredient2 = makeIngredient("Margarine", 5);
        Ingredient ingredient3 = makeIngredient("Marshmallow", 6);
        Ingredient ingredient4 = makeIngredient("Vanilla", 7);
        Ingredient[] ingredients = { ingredient1, ingredient2, ingredient3, ingredient4 };
        Snack snack = new Snack(
                "Rice Crispie Treat",
                true,
                false,
                2,
                ingredients
        );
        snack.setId(1);
        snack.setCreatedAt(DATE);
        snack.setUpdatedAt(DATE);

        Team team = new Team(
                "Mules",
                Rink.BAIREL,
                Level.D5,
                "#b88907",
                "#000000",
                "#c42323",
                "logo.com"
        );
        team.setId(1);
        team.setCreatedAt(DATE);
        team.setUpdatedAt(DATE);

        // create the one to send in the request
        SnackLog snackLogEntry = new SnackLog(snack, team, LocalDate.of(2026, Month.JUNE, 1));

        //--------------------------------------------------
        // CONFIGURE MOCKS
        when(snackLogRepo.save(any())).thenThrow(new OptimisticLockingFailureException("DB conflict"));

        //--------------------------------------------------
        // EXECUTE & VERIFY RESULTS
        mockMvc.perform(post("/snack-log")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(snackLogEntry))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString())); // print the response
    }

    @Test
    void test_addToSnackLog_error() throws Exception {
        //--------------------------------------------------
        // SET VALUES
        Ingredient ingredient1 = makeIngredient("Rice Crispy Cereal", 4);
        Ingredient ingredient2 = makeIngredient("Margarine", 5);
        Ingredient ingredient3 = makeIngredient("Marshmallow", 6);
        Ingredient ingredient4 = makeIngredient("Vanilla", 7);
        Ingredient[] ingredients = { ingredient1, ingredient2, ingredient3, ingredient4 };
        Snack snack = new Snack(
                "Rice Crispie Treat",
                true,
                false,
                2,
                ingredients
        );
        snack.setId(1);
        snack.setCreatedAt(DATE);
        snack.setUpdatedAt(DATE);

        Team team = new Team(
                "Mules",
                Rink.BAIREL,
                Level.D5,
                "#b88907",
                "#000000",
                "#c42323",
                "logo.com"
        );
        team.setId(1);
        team.setCreatedAt(DATE);
        team.setUpdatedAt(DATE);

        // create the one to send in the request
        SnackLog snackLogEntry = new SnackLog(snack, team, LocalDate.of(2026, Month.JUNE, 1));

        //--------------------------------------------------
        // CONFIGURE MOCKS
        when(snackLogRepo.save(any())).thenThrow(new IllegalArgumentException("DB error"));

        //--------------------------------------------------
        // EXECUTE & VERIFY RESULTS
        mockMvc.perform(post("/snack-log")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(snackLogEntry))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString())); // print the response
    }



    /**
     * Helper function to make a new ingredient and set all fields
     * @param name - the name of the ingredient
     * @param id - the id to set for the ingredient
     * @return the new ingredient, with the id and created/updated fields set
     */
    Ingredient makeIngredient(String name, int id) {
        Ingredient ingredient = new Ingredient(name);
        ingredient.setId(id);
        ingredient.setCreatedAt(DATE);
        ingredient.setUpdatedAt(DATE);
        return ingredient;
    }
}
