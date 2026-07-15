package org.bnbalint.snackdaddy.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bnbalint.snackdaddy.models.*;
import org.bnbalint.snackdaddy.repositories.SnackRepository;
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

@WebMvcTest(SnackController.class)
public class SnackControllerTest {

    static Instant DATE = Instant.parse("2026-07-01T00:00:01Z");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // Spring automatically provides this instance


    @MockBean
    private SnackRepository snackRepo;


    //---------------------------------------------------------------
    // getAllSnacks
    //

    @Test
    void test_getAllSnacks_success() throws Exception {
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
        System.out.println("Snack = " + snack);

        //--------------------------------------------------
        // CONFIGURE MOCKS
        when(snackRepo.findAll()).thenReturn(List.of(snack));

        //--------------------------------------------------
        // EXECUTE & VERIFY RESULTS
        mockMvc.perform(get("/snacks")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print()) // print the response
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].name").value("Rice Crispie Treat"))
                .andExpect(jsonPath("$.[0].sweet").value("true"))
                .andExpect(jsonPath("$.[0].savory").value("false"))
                .andExpect(jsonPath("$.[0].difficulty").value(2))
                .andExpect(jsonPath("$.[0].ingredients").isNotEmpty())
                .andExpect(jsonPath("$.[0].created_at").value(DATE.atOffset(ZoneOffset.UTC).toString()))
                .andExpect(jsonPath("$.[0].updated_at").value(DATE.atOffset(ZoneOffset.UTC).toString()));
    }


    @Test
    void test_getAllSnacks_error() throws Exception {
        //--------------------------------------------------
        // SET VALUES

        //--------------------------------------------------
        // CONFIGURE MOCKS
        when(snackRepo.findAll()).thenThrow(new IllegalArgumentException("DB error"));

        //--------------------------------------------------
        // EXECUTE & VERIFY RESULTS
        mockMvc.perform(get("/snacks")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andDo(print()); // print the response
    }

    //---------------------------------------------------------------
    // addSnack
    //
    @Test
    void test_addSnack_success() throws Exception {
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

        Snack savedSnack = new Snack(
                "Rice Crispie Treat",
                true,
                false,
                2,
                ingredients
        );
        savedSnack.setId(1);
        savedSnack.setCreatedAt(DATE);
        savedSnack.setUpdatedAt(DATE);

        //--------------------------------------------------
        // CONFIGURE MOCKS
        when(snackRepo.save(any())).thenReturn(savedSnack);

        //--------------------------------------------------
        // EXECUTE & VERIFY RESULTS
        mockMvc.perform(post("/snacks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(snack))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString())) // print the response
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Rice Crispie Treat"))
                .andExpect(jsonPath("$.sweet").value("true"))
                .andExpect(jsonPath("$.savory").value("false"))
                .andExpect(jsonPath("$.difficulty").value(2))
                .andExpect(jsonPath("$.ingredients").isNotEmpty())
                .andExpect(jsonPath("$.created_at").value(DATE.atOffset(ZoneOffset.UTC).toString()))
                .andExpect(jsonPath("$.updated_at").value(DATE.atOffset(ZoneOffset.UTC).toString()));
    }

    @Test
    void test_addSnack_conflict() throws Exception {
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

        //--------------------------------------------------
        // CONFIGURE MOCKS
        when(snackRepo.save(any())).thenThrow(new OptimisticLockingFailureException("DB conflict"));

        //--------------------------------------------------
        // EXECUTE & VERIFY RESULTS
        mockMvc.perform(post("/snacks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(snack))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString())); // print the response
    }

    @Test
    void test_addSnack_error() throws Exception {
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
        //--------------------------------------------------
        // CONFIGURE MOCKS
        when(snackRepo.save(any())).thenThrow(new IllegalArgumentException("DB error"));

        //--------------------------------------------------
        // EXECUTE & VERIFY RESULTS
        mockMvc.perform(post("/snacks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(snack))
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
