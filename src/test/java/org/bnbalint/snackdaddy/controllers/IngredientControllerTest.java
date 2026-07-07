package org.bnbalint.snackdaddy.controllers;

import org.bnbalint.snackdaddy.models.Ingredient;
import org.bnbalint.snackdaddy.repositories.IngredientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.List;
import static org.mockito.Mockito.when;

@WebMvcTest(IngredientController.class)
public class IngredientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IngredientRepository ingredientRepo;


    @Test
    void test_getAllIngredients_success() throws Exception {
        //--------------------------------------------------
        // SET VALUES
        Ingredient ingredient = new Ingredient("Pecan");
        ingredient.setId(1);

        //--------------------------------------------------
        // CONFIGURE MOCKS
        when(ingredientRepo.findAll()).thenReturn(List.of(ingredient));

        //--------------------------------------------------
        // EXECUTE & VERIFY RESULTS
        mockMvc.perform(get("/ingredients")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].name").value("Pecan"));
    }
}
