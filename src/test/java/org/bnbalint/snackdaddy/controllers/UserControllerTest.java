package org.bnbalint.snackdaddy.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bnbalint.snackdaddy.models.*;
import org.bnbalint.snackdaddy.repositories.UserRepository;
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

@WebMvcTest(UserController.class)
public class UserControllerTest {

    static Instant DATE = Instant.parse("2026-07-01T00:00:01Z");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // Spring automatically provides this instance


    @MockBean
    private UserRepository userRepo;


    //---------------------------------------------------------------
    // getAllUsers
    //

    @Test
    void test_getAllUsers_success() throws Exception {
        //--------------------------------------------------
        // SET VALUES
        Team team = new Team(
                "Mules",
                Rink.BAIREL,
                Level.D5,
                "#b88907",
                "#000000",
                "#c42323",
                ""
        );
        team.setId(1);
        team.setCreatedAt(DATE);
        team.setUpdatedAt(DATE);
        Team[] teams = { team };

        Ingredient ingredient = new Ingredient("Pecan");
        ingredient.setId(1);
        ingredient.setCreatedAt(DATE);
        ingredient.setUpdatedAt(DATE);
        Ingredient[] allergies = { ingredient };

        User user = new User("Roger", "Hogwarts", "r.h@gmail.com", teams, allergies);
        user.setId(1);
        user.setCreatedAt(DATE);
        user.setUpdatedAt(DATE);
        System.out.println("User = " + user);

        //--------------------------------------------------
        // CONFIGURE MOCKS
        when(userRepo.findAll()).thenReturn(List.of(user));

        //--------------------------------------------------
        // EXECUTE & VERIFY RESULTS
        mockMvc.perform(get("/users")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print()) // print the response
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].first_name").value("Roger"))
                .andExpect(jsonPath("$.[0].last_name").value("Hogwarts"))
                .andExpect(jsonPath("$.[0].email").value("r.h@gmail.com"))
                .andExpect(jsonPath("$.[0].teams").isArray())
                .andExpect(jsonPath("$.[0].allergies").isArray())
                .andExpect(jsonPath("$.[0].created_at").value(DATE.atOffset(ZoneOffset.UTC).toString()))
                .andExpect(jsonPath("$.[0].updated_at").value(DATE.atOffset(ZoneOffset.UTC).toString()));
    }


    @Test
    void test_getAllUsers_error() throws Exception {
        //--------------------------------------------------
        // SET VALUES

        //--------------------------------------------------
        // CONFIGURE MOCKS
        when(userRepo.findAll()).thenThrow(new IllegalArgumentException("DB error"));

        //--------------------------------------------------
        // EXECUTE & VERIFY RESULTS
        mockMvc.perform(get("/users")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andDo(print()); // print the response
    }

    //---------------------------------------------------------------
    // addUser
    //
    @Test
    void test_addUser_success() throws Exception {
        //--------------------------------------------------
        // SET VALUES
        Team team = new Team(
                "Mules",
                Rink.BAIREL,
                Level.D5,
                "#b88907",
                "#000000",
                "#c42323",
                ""
        );
        team.setId(1);
        team.setCreatedAt(DATE);
        team.setUpdatedAt(DATE);
        Team[] teams = { team };

        Ingredient ingredient = new Ingredient("Pecan");
        ingredient.setId(1);
        ingredient.setCreatedAt(DATE);
        ingredient.setUpdatedAt(DATE);
        Ingredient[] allergies = { ingredient };

        // create the one to send in the request
        User user = new User("Roger", "Hogwarts", "r.h@gmail.com", teams, allergies);

        // create the one to return from the mock database
        User savedUser = new User("Roger", "Hogwarts", "r.h@gmail.com", teams, allergies);
        savedUser.setId(1);
        savedUser.setCreatedAt(DATE);
        savedUser.setUpdatedAt(DATE);

        //--------------------------------------------------
        // CONFIGURE MOCKS
        when(userRepo.save(any())).thenReturn(savedUser);

        //--------------------------------------------------
        // EXECUTE & VERIFY RESULTS
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString())) // print the response
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.first_name").value("Roger"))
                .andExpect(jsonPath("$.last_name").value("Hogwarts"))
                .andExpect(jsonPath("$.email").value("r.h@gmail.com"))
                .andExpect(jsonPath("$.teams").isArray())
                .andExpect(jsonPath("$.allergies").isArray())
                .andExpect(jsonPath("$.created_at").value(DATE.atOffset(ZoneOffset.UTC).toString()))
                .andExpect(jsonPath("$.updated_at").value(DATE.atOffset(ZoneOffset.UTC).toString()));
    }

    @Test
    void test_addUser_conflict() throws Exception {
        //--------------------------------------------------
        // SET VALUES
        Team team = new Team(
                "Mules",
                Rink.BAIREL,
                Level.D5,
                "#b88907",
                "#000000",
                "#c42323",
                ""
        );
        team.setId(1);
        team.setCreatedAt(DATE);
        team.setUpdatedAt(DATE);
        Team[] teams = { team };

        Ingredient ingredient = new Ingredient("Pecan");
        ingredient.setId(1);
        ingredient.setCreatedAt(DATE);
        ingredient.setUpdatedAt(DATE);
        Ingredient[] allergies = { ingredient };

        User user = new User("Roger", "Hogwarts", "r.h@gmail.com", teams, allergies);
        System.out.println("User = " + user);

        //--------------------------------------------------
        // CONFIGURE MOCKS
        when(userRepo.save(any())).thenThrow(new OptimisticLockingFailureException("DB conflict"));

        //--------------------------------------------------
        // EXECUTE & VERIFY RESULTS
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString())); // print the response
    }

    @Test
    void test_addUser_error() throws Exception {
        //--------------------------------------------------
        // SET VALUES
        Team team = new Team(
                "Mules",
                Rink.BAIREL,
                Level.D5,
                "#b88907",
                "#000000",
                "#c42323",
                ""
        );
        team.setId(1);
        team.setCreatedAt(DATE);
        team.setUpdatedAt(DATE);
        Team[] teams = { team };

        Ingredient ingredient = new Ingredient("Pecan");
        ingredient.setId(1);
        ingredient.setCreatedAt(DATE);
        ingredient.setUpdatedAt(DATE);
        Ingredient[] allergies = { ingredient };

        User user = new User("Roger", "Hogwarts", "r.h@gmail.com", teams, allergies);
        System.out.println("User = " + user);

        //--------------------------------------------------
        // CONFIGURE MOCKS
        when(userRepo.save(any())).thenThrow(new IllegalArgumentException("DB error"));

        //--------------------------------------------------
        // EXECUTE & VERIFY RESULTS
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString())); // print the response
    }
}
