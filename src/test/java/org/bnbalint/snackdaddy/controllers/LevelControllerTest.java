package org.bnbalint.snackdaddy.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(LevelController.class)
public class LevelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void test_getAllLevels_success() throws Exception {

        //--------------------------------------------------
        // EXECUTE & VERIFY RESULTS
        mockMvc.perform(get("/levels")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print()) // print the response
                .andExpect(jsonPath("$.[0]").value("D5"))
                .andExpect(jsonPath("$.[1]").value("D4"))
                .andExpect(jsonPath("$.[2]").value("D3"));
    }
}
