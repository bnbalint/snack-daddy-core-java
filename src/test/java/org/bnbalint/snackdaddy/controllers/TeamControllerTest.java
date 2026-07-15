package org.bnbalint.snackdaddy.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bnbalint.snackdaddy.models.Level;
import org.bnbalint.snackdaddy.models.Rink;
import org.bnbalint.snackdaddy.models.Team;
import org.bnbalint.snackdaddy.repositories.TeamRepository;
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

@WebMvcTest(TeamController.class)
public class TeamControllerTest {

    static Instant DATE = Instant.parse("2026-07-01T00:00:01Z");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // Spring automatically provides this instance


    @MockBean
    private TeamRepository teamRepo;


    //---------------------------------------------------------------
    // getAllTeams
    //

    @Test
    void test_getAllTeams_success() throws Exception {
        //--------------------------------------------------
        // SET VALUES
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
        System.out.println("Team = " + team);

        //--------------------------------------------------
        // CONFIGURE MOCKS
        when(teamRepo.findAll()).thenReturn(List.of(team));

        //--------------------------------------------------
        // EXECUTE & VERIFY RESULTS
        mockMvc.perform(get("/teams")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print()) // print the response
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].name").value("Mules"))
                .andExpect(jsonPath("$.[0].rink").value("BAIREL"))
                .andExpect(jsonPath("$.[0].level").value("D5"))
                .andExpect(jsonPath("$.[0].primary_color").value("#b88907"))
                .andExpect(jsonPath("$.[0].secondary_color").value("#000000"))
                .andExpect(jsonPath("$.[0].ternary_color").value("#c42323"))
                .andExpect(jsonPath("$.[0].logo_url").value("logo.com"))
                .andExpect(jsonPath("$.[0].created_at").value(DATE.atOffset(ZoneOffset.UTC).toString()))
                .andExpect(jsonPath("$.[0].updated_at").value(DATE.atOffset(ZoneOffset.UTC).toString()));
    }


    @Test
    void test_getAllTeams_error() throws Exception {
        //--------------------------------------------------
        // SET VALUES

        //--------------------------------------------------
        // CONFIGURE MOCKS
        when(teamRepo.findAll()).thenThrow(new IllegalArgumentException("DB error"));

        //--------------------------------------------------
        // EXECUTE & VERIFY RESULTS
        mockMvc.perform(get("/teams")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andDo(print()); // print the response
    }

    //---------------------------------------------------------------
    // addTeam
    //
    @Test
    void test_addTeam_success() throws Exception {
        //--------------------------------------------------
        // SET VALUES
        Team team = new Team(
                "Mules",
                Rink.BAIREL,
                Level.D5,
                "#b88907",
                "#000000",
                "#c42323",
                "logo.com"
        );

        Team savedTeam = new Team(
                "Mules",
                Rink.BAIREL,
                Level.D5,
                "#b88907",
                "#000000",
                "#c42323",
                "logo.com"
        );
        savedTeam.setId(1);
        savedTeam.setCreatedAt(DATE);
        savedTeam.setUpdatedAt(DATE);

        //--------------------------------------------------
        // CONFIGURE MOCKS
        when(teamRepo.save(any())).thenReturn(savedTeam);

        //--------------------------------------------------
        // EXECUTE & VERIFY RESULTS
        mockMvc.perform(post("/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(team))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString())) // print the response
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Mules"))
                .andExpect(jsonPath("$.rink").value("BAIREL"))
                .andExpect(jsonPath("$.level").value("D5"))
                .andExpect(jsonPath("$.primary_color").value("#b88907"))
                .andExpect(jsonPath("$.secondary_color").value("#000000"))
                .andExpect(jsonPath("$.ternary_color").value("#c42323"))
                .andExpect(jsonPath(".logo_url").value("logo.com"))
                .andExpect(jsonPath("$.created_at").value(DATE.atOffset(ZoneOffset.UTC).toString()))
                .andExpect(jsonPath("$.updated_at").value(DATE.atOffset(ZoneOffset.UTC).toString()));
    }

    @Test
    void test_addTeam_conflict() throws Exception {
        //--------------------------------------------------
        // SET VALUES
        Team team = new Team(
                "Mules",
                Rink.BAIREL,
                Level.D5,
                "#b88907",
                "#000000",
                "#c42323",
                "logo.com"
        );

        //--------------------------------------------------
        // CONFIGURE MOCKS
        when(teamRepo.save(any())).thenThrow(new OptimisticLockingFailureException("DB conflict"));

        //--------------------------------------------------
        // EXECUTE & VERIFY RESULTS
        mockMvc.perform(post("/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(team))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString())); // print the response
    }

    @Test
    void test_addTeam_error() throws Exception {
        //--------------------------------------------------
        // SET VALUES
        Team team = new Team(
                "Mules",
                Rink.BAIREL,
                Level.D5,
                "#b88907",
                "#000000",
                "#c42323",
                "logo.com"
        );

        //--------------------------------------------------
        // CONFIGURE MOCKS
        when(teamRepo.save(any())).thenThrow(new IllegalArgumentException("DB error"));

        //--------------------------------------------------
        // EXECUTE & VERIFY RESULTS
        mockMvc.perform(post("/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(team))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString())); // print the response
    }
}
