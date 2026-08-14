package org.bnbalint.snackdaddy.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bnbalint.snackdaddy.models.*;
import org.bnbalint.snackdaddy.repositories.UserSnackRankingRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserSnackRankingController.class)
public class UserSnackRankingControllerTest {

    static Instant DATE = Instant.parse("2026-07-01T00:00:01Z");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // Spring automatically provides this instance


    @MockBean
    private UserSnackRankingRepository userSnackRankingRepo;


    //---------------------------------------------------------------
    // getAllRankings
    //

    @Test
    void test_getAllRankings_success() throws Exception {
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
        team.setId(1L);
        team.setCreatedAt(DATE);
        team.setUpdatedAt(DATE);
        Team[] teams = { team };

        Ingredient ingredient = new Ingredient("Pecan");
        ingredient.setId(1L);
        ingredient.setCreatedAt(DATE);
        ingredient.setUpdatedAt(DATE);
        Ingredient[] allergies = { ingredient };

        User user = new User("Roger", "Hogwarts", "r.h@gmail.com", teams, allergies);
        user.setId(1L);
        user.setCreatedAt(DATE);
        user.setUpdatedAt(DATE);
        System.out.println("User = " + user);

        Ingredient ingredient1 = makeIngredient("Rice Crispy Cereal", 4L);
        Ingredient ingredient2 = makeIngredient("Margarine", 5L);
        Ingredient ingredient3 = makeIngredient("Marshmallow", 6L);
        Ingredient ingredient4 = makeIngredient("Vanilla", 7L);
        Ingredient[] ingredients = { ingredient1, ingredient2, ingredient3, ingredient4 };
        Snack snack = new Snack(
                "Rice Crispie Treat",
                true,
                false,
                2,
                ingredients
        );
        snack.setId(1L);
        snack.setCreatedAt(DATE);
        snack.setUpdatedAt(DATE);
        System.out.println("Snack = " + snack);

        UserSnackRanking ranking = new UserSnackRanking(snack, user, SnackRank.RANK_10);
        ranking.setCreatedAt(DATE);
        ranking.setUpdatedAt(DATE);
        System.out.println("Ranking = " + ranking);

        //--------------------------------------------------
        // CONFIGURE MOCKS
        when(userSnackRankingRepo.findAll()).thenReturn(List.of(ranking));

        //--------------------------------------------------
        // EXECUTE & VERIFY RESULTS
        mockMvc.perform(get("/snack-rankings")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print()) // print the response
                .andExpect(jsonPath("$.[0].rank").value("RANK_10"))
                .andExpect(jsonPath("$.[0].created_at").value(DATE.atOffset(ZoneOffset.UTC).toString()))
                .andExpect(jsonPath("$.[0].updated_at").value(DATE.atOffset(ZoneOffset.UTC).toString()))
                .andExpect(jsonPath("$.[0].user.id").value(1))
                .andExpect(jsonPath("$.[0].user.first_name").value("Roger"))
                .andExpect(jsonPath("$.[0].user.last_name").value("Hogwarts"))
                .andExpect(jsonPath("$.[0].user.email").value("r.h@gmail.com"))
                .andExpect(jsonPath("$.[0].user.teams").isArray())
                .andExpect(jsonPath("$.[0].user.allergies").isArray())
                .andExpect(jsonPath("$.[0].user.created_at").value(DATE.atOffset(ZoneOffset.UTC).toString()))
                .andExpect(jsonPath("$.[0].user.updated_at").value(DATE.atOffset(ZoneOffset.UTC).toString()))
                .andExpect(jsonPath("$.[0].snack.id").value(1))
                .andExpect(jsonPath("$.[0].snack.name").value("Rice Crispie Treat"))
                .andExpect(jsonPath("$.[0].snack.sweet").value("true"))
                .andExpect(jsonPath("$.[0].snack.savory").value("false"))
                .andExpect(jsonPath("$.[0].snack.difficulty").value(2))
                .andExpect(jsonPath("$.[0].snack.ingredients").isNotEmpty())
                .andExpect(jsonPath("$.[0].snack.created_at").value(DATE.atOffset(ZoneOffset.UTC).toString()))
                .andExpect(jsonPath("$.[0].snack.updated_at").value(DATE.atOffset(ZoneOffset.UTC).toString()));
    }


    @Test
    void test_getAllRankings_error() throws Exception {
        //--------------------------------------------------
        // SET VALUES

        //--------------------------------------------------
        // CONFIGURE MOCKS
        when(userSnackRankingRepo.findAll()).thenThrow(new IllegalArgumentException("DB error"));

        //--------------------------------------------------
        // EXECUTE & VERIFY RESULTS
        mockMvc.perform(get("/snack-rankings")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andDo(print()); // print the response
    }



    //---------------------------------------------------------------
    // addUserSnackRanking
    //
    @Test
    void test_addUserSnackRanking_success() throws Exception {
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
        team.setId(1L);
        team.setCreatedAt(DATE);
        team.setUpdatedAt(DATE);
        Team[] teams = { team };

        Ingredient ingredient = new Ingredient("Pecan");
        ingredient.setId(1L);
        ingredient.setCreatedAt(DATE);
        ingredient.setUpdatedAt(DATE);
        Ingredient[] allergies = { ingredient };

        User user = new User("Roger", "Hogwarts", "r.h@gmail.com", teams, allergies);
        user.setId(1L);
        user.setCreatedAt(DATE);
        user.setUpdatedAt(DATE);
        System.out.println("User = " + user);

        Ingredient ingredient1 = makeIngredient("Rice Crispy Cereal", 4L);
        Ingredient ingredient2 = makeIngredient("Margarine", 5L);
        Ingredient ingredient3 = makeIngredient("Marshmallow", 6L);
        Ingredient ingredient4 = makeIngredient("Vanilla", 7L);
        Ingredient[] ingredients = { ingredient1, ingredient2, ingredient3, ingredient4 };
        Snack snack = new Snack(
                "Rice Crispie Treat",
                true,
                false,
                2,
                ingredients
        );
        snack.setId(1L);
        snack.setCreatedAt(DATE);
        snack.setUpdatedAt(DATE);
        System.out.println("Snack = " + snack);

        // create the one to send in the request
        UserSnackRanking ranking = new UserSnackRanking(snack, user, SnackRank.RANK_10);
        System.out.println("Ranking = " + ranking);

        // create the one to return from the mock database
        UserSnackRanking savedRanking = new UserSnackRanking(snack, user, SnackRank.RANK_10);
        savedRanking.setCreatedAt(DATE);
        savedRanking.setUpdatedAt(DATE);

        //--------------------------------------------------
        // CONFIGURE MOCKS
        when(userSnackRankingRepo.save(any())).thenReturn(savedRanking);

        //--------------------------------------------------
        // EXECUTE & VERIFY RESULTS
        mockMvc.perform(post("/snack-rankings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ranking))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString())) // print the response
                .andExpect(jsonPath("$.rank").value("RANK_10"))
                .andExpect(jsonPath("$.created_at").value(DATE.atOffset(ZoneOffset.UTC).toString()))
                .andExpect(jsonPath("$.updated_at").value(DATE.atOffset(ZoneOffset.UTC).toString()))
                .andExpect(jsonPath("$.user.id").value(1))
                .andExpect(jsonPath("$.user.first_name").value("Roger"))
                .andExpect(jsonPath("$.user.last_name").value("Hogwarts"))
                .andExpect(jsonPath("$.user.email").value("r.h@gmail.com"))
                .andExpect(jsonPath("$.user.teams").isArray())
                .andExpect(jsonPath("$.user.allergies").isArray())
                .andExpect(jsonPath("$.user.created_at").value(DATE.atOffset(ZoneOffset.UTC).toString()))
                .andExpect(jsonPath("$.user.updated_at").value(DATE.atOffset(ZoneOffset.UTC).toString()))
                .andExpect(jsonPath("$.snack.id").value(1))
                .andExpect(jsonPath("$.snack.name").value("Rice Crispie Treat"))
                .andExpect(jsonPath("$.snack.sweet").value("true"))
                .andExpect(jsonPath("$.snack.savory").value("false"))
                .andExpect(jsonPath("$.snack.difficulty").value(2))
                .andExpect(jsonPath("$.snack.ingredients").isNotEmpty())
                .andExpect(jsonPath("$.snack.created_at").value(DATE.atOffset(ZoneOffset.UTC).toString()))
                .andExpect(jsonPath("$.snack.updated_at").value(DATE.atOffset(ZoneOffset.UTC).toString()));
    }

    @Test
    void test_addUserSnackRanking_conflict() throws Exception {
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
        team.setId(1L);
        team.setCreatedAt(DATE);
        team.setUpdatedAt(DATE);
        Team[] teams = { team };

        Ingredient ingredient = new Ingredient("Pecan");
        ingredient.setId(1L);
        ingredient.setCreatedAt(DATE);
        ingredient.setUpdatedAt(DATE);
        Ingredient[] allergies = { ingredient };

        User user = new User("Roger", "Hogwarts", "r.h@gmail.com", teams, allergies);
        user.setId(1L);
        user.setCreatedAt(DATE);
        user.setUpdatedAt(DATE);
        System.out.println("User = " + user);

        Ingredient ingredient1 = makeIngredient("Rice Crispy Cereal", 4L);
        Ingredient ingredient2 = makeIngredient("Margarine", 5L);
        Ingredient ingredient3 = makeIngredient("Marshmallow", 6L);
        Ingredient ingredient4 = makeIngredient("Vanilla", 7L);
        Ingredient[] ingredients = { ingredient1, ingredient2, ingredient3, ingredient4 };
        Snack snack = new Snack(
                "Rice Crispie Treat",
                true,
                false,
                2,
                ingredients
        );
        snack.setId(1L);
        snack.setCreatedAt(DATE);
        snack.setUpdatedAt(DATE);
        System.out.println("Snack = " + snack);

        // create the one to send in the request
        UserSnackRanking ranking = new UserSnackRanking(snack, user, SnackRank.RANK_10);
        System.out.println("Ranking = " + ranking);

        //--------------------------------------------------
        // CONFIGURE MOCKS
        when(userSnackRankingRepo.save(any())).thenThrow(new OptimisticLockingFailureException("DB conflict"));

        //--------------------------------------------------
        // EXECUTE & VERIFY RESULTS
        mockMvc.perform(post("/snack-rankings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ranking))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString())); // print the response
    }

    @Test
    void test_addUserSnackRanking_error() throws Exception {
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
        team.setId(1L);
        team.setCreatedAt(DATE);
        team.setUpdatedAt(DATE);
        Team[] teams = { team };

        Ingredient ingredient = new Ingredient("Pecan");
        ingredient.setId(1L);
        ingredient.setCreatedAt(DATE);
        ingredient.setUpdatedAt(DATE);
        Ingredient[] allergies = { ingredient };

        User user = new User("Roger", "Hogwarts", "r.h@gmail.com", teams, allergies);
        user.setId(1L);
        user.setCreatedAt(DATE);
        user.setUpdatedAt(DATE);
        System.out.println("User = " + user);

        Ingredient ingredient1 = makeIngredient("Rice Crispy Cereal", 4L);
        Ingredient ingredient2 = makeIngredient("Margarine", 5L);
        Ingredient ingredient3 = makeIngredient("Marshmallow", 6L);
        Ingredient ingredient4 = makeIngredient("Vanilla", 7L);
        Ingredient[] ingredients = { ingredient1, ingredient2, ingredient3, ingredient4 };
        Snack snack = new Snack(
                "Rice Crispie Treat",
                true,
                false,
                2,
                ingredients
        );
        snack.setId(1L);
        snack.setCreatedAt(DATE);
        snack.setUpdatedAt(DATE);
        System.out.println("Snack = " + snack);

        // create the one to send in the request
        UserSnackRanking ranking = new UserSnackRanking(snack, user, SnackRank.RANK_10);
        System.out.println("Ranking = " + ranking);

        //--------------------------------------------------
        // CONFIGURE MOCKS
        when(userSnackRankingRepo.save(any())).thenThrow(new IllegalArgumentException("DB error"));

        //--------------------------------------------------
        // EXECUTE & VERIFY RESULTS
        mockMvc.perform(post("/snack-rankings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ranking))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString())); // print the response
    }


    //---------------------------------------------------------------
    // getRankingsByUserId
    //

    @Test
    void test_getRankingsByUserId_success() throws Exception {
        //--------------------------------------------------
        // SET VALUES
        long userId = 1L;
        Team team = new Team(
                "Mules",
                Rink.BAIREL,
                Level.D5,
                "#b88907",
                "#000000",
                "#c42323",
                ""
        );
        team.setId(1L);
        team.setCreatedAt(DATE);
        team.setUpdatedAt(DATE);
        Team[] teams = { team };

        Ingredient ingredient = new Ingredient("Pecan");
        ingredient.setId(1L);
        ingredient.setCreatedAt(DATE);
        ingredient.setUpdatedAt(DATE);
        Ingredient[] allergies = { ingredient };

        User user = new User("Roger", "Hogwarts", "r.h@gmail.com", teams, allergies);
        user.setId(userId);
        user.setCreatedAt(DATE);
        user.setUpdatedAt(DATE);
        System.out.println("User = " + user);

        Ingredient ingredient1 = makeIngredient("Rice Crispy Cereal", 4L);
        Ingredient ingredient2 = makeIngredient("Margarine", 5L);
        Ingredient ingredient3 = makeIngredient("Marshmallow", 6L);
        Ingredient ingredient4 = makeIngredient("Vanilla", 7L);
        Ingredient[] ingredients = { ingredient1, ingredient2, ingredient3, ingredient4 };
        Snack snack = new Snack(
                "Rice Crispie Treat",
                true,
                false,
                2,
                ingredients
        );
        snack.setId(1L);
        snack.setCreatedAt(DATE);
        snack.setUpdatedAt(DATE);
        System.out.println("Snack = " + snack);

        UserSnackRanking ranking = new UserSnackRanking(snack, user, SnackRank.RANK_10);
        ranking.setCreatedAt(DATE);
        ranking.setUpdatedAt(DATE);
        System.out.println("Ranking = " + ranking);

        //--------------------------------------------------
        // CONFIGURE MOCKS
        when(userSnackRankingRepo.findAllByUserId(userId)).thenReturn(List.of(ranking));

        //--------------------------------------------------
        // EXECUTE & VERIFY RESULTS
        mockMvc.perform(get("/users/"+ userId + "/snack-rankings")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print()) // print the response
                .andExpect(jsonPath("$.[0].rank").value("RANK_10"))
                .andExpect(jsonPath("$.[0].created_at").value(DATE.atOffset(ZoneOffset.UTC).toString()))
                .andExpect(jsonPath("$.[0].updated_at").value(DATE.atOffset(ZoneOffset.UTC).toString()))
                .andExpect(jsonPath("$.[0].user.id").value(1))
                .andExpect(jsonPath("$.[0].user.first_name").value("Roger"))
                .andExpect(jsonPath("$.[0].user.last_name").value("Hogwarts"))
                .andExpect(jsonPath("$.[0].user.email").value("r.h@gmail.com"))
                .andExpect(jsonPath("$.[0].user.teams").isArray())
                .andExpect(jsonPath("$.[0].user.allergies").isArray())
                .andExpect(jsonPath("$.[0].user.created_at").value(DATE.atOffset(ZoneOffset.UTC).toString()))
                .andExpect(jsonPath("$.[0].user.updated_at").value(DATE.atOffset(ZoneOffset.UTC).toString()))
                .andExpect(jsonPath("$.[0].snack.id").value(1))
                .andExpect(jsonPath("$.[0].snack.name").value("Rice Crispie Treat"))
                .andExpect(jsonPath("$.[0].snack.sweet").value("true"))
                .andExpect(jsonPath("$.[0].snack.savory").value("false"))
                .andExpect(jsonPath("$.[0].snack.difficulty").value(2))
                .andExpect(jsonPath("$.[0].snack.ingredients").isNotEmpty())
                .andExpect(jsonPath("$.[0].snack.created_at").value(DATE.atOffset(ZoneOffset.UTC).toString()))
                .andExpect(jsonPath("$.[0].snack.updated_at").value(DATE.atOffset(ZoneOffset.UTC).toString()));
    }

    @Test
    void test_getRankingsByUserId_nullUserId() throws Exception {
        //--------------------------------------------------
        // SET VALUES
        Long userId = null;

        //--------------------------------------------------
        // EXECUTE & VERIFY RESULTS
        mockMvc.perform(get("/users/"+ userId + "/snack-rankings")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print()); // print the response
    }

    @Test
    void test_getRankingsByUserId_negativeUserId() throws Exception {
        //--------------------------------------------------
        // SET VALUES
        long userId = -2L;

        //--------------------------------------------------
        // EXECUTE & VERIFY RESULTS
        mockMvc.perform(get("/users/"+ userId + "/snack-rankings")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print()); // print the response
    }

    @Test
    void test_getRankingsByUserId_error() throws Exception {
        //--------------------------------------------------
        // SET VALUES
        long userId = 1L;

        //--------------------------------------------------
        // CONFIGURE MOCKS
        when(userSnackRankingRepo.findAllByUserId(userId)).thenThrow(new IllegalArgumentException("database error"));

        //--------------------------------------------------
        // EXECUTE & VERIFY RESULTS
        mockMvc.perform(get("/users/"+ userId + "/snack-rankings")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andDo(print()); // print the response
    }


    //---------------------------------------------------------------
    // updateUserSnackRankings
    //
    @Test
    void test_updateUserSnackRankings_success() throws Exception {
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
        team.setId(1L);
        team.setCreatedAt(DATE);
        team.setUpdatedAt(DATE);
        Team[] teams = { team };

        Ingredient ingredient = new Ingredient("Pecan");
        ingredient.setId(1L);
        ingredient.setCreatedAt(DATE);
        ingredient.setUpdatedAt(DATE);
        Ingredient[] allergies = { ingredient };

        User user = new User("Roger", "Hogwarts", "r.h@gmail.com", teams, allergies);
        user.setId(1L);
        user.setCreatedAt(DATE);
        user.setUpdatedAt(DATE);
        System.out.println("User = " + user);

        Ingredient ingredient1 = makeIngredient("Rice Crispy Cereal", 4L);
        Ingredient ingredient2 = makeIngredient("Margarine", 5L);
        Ingredient ingredient3 = makeIngredient("Marshmallow", 6L);
        Ingredient ingredient4 = makeIngredient("Vanilla", 7L);
        Ingredient[] ingredients = { ingredient1, ingredient2, ingredient3, ingredient4 };
        Snack snack = new Snack(
                "Rice Crispie Treat",
                true,
                false,
                2,
                ingredients
        );
        snack.setId(1L);
        snack.setCreatedAt(DATE);
        snack.setUpdatedAt(DATE);
        System.out.println("Snack = " + snack);

        // create the one to send in the request
        UserSnackRanking ranking = new UserSnackRanking(snack, user, SnackRank.RANK_10);
        ranking.setCreatedAt(DATE);
        ranking.setUpdatedAt(DATE);
        System.out.println("Ranking = " + ranking);

        // create the one to return from the mock database
        UserSnackRanking savedRanking = new UserSnackRanking(snack, user, SnackRank.RANK_10);
        savedRanking.setCreatedAt(DATE);
        savedRanking.setUpdatedAt(DATE);

        //--------------------------------------------------
        // CONFIGURE MOCKS
        when(userSnackRankingRepo.saveAllAndFlush(any())).thenReturn(List.of(savedRanking));

        //--------------------------------------------------
        // EXECUTE & VERIFY RESULTS
        mockMvc.perform(put("/snack-rankings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(ranking)))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString())) // print the response
                .andExpect(jsonPath("$.[0].rank").value("RANK_10"))
                .andExpect(jsonPath("$.[0].created_at").value(DATE.atOffset(ZoneOffset.UTC).toString()))
                .andExpect(jsonPath("$.[0].updated_at").value(DATE.atOffset(ZoneOffset.UTC).toString()))
                .andExpect(jsonPath("$.[0].user.id").value(1))
                .andExpect(jsonPath("$.[0].user.first_name").value("Roger"))
                .andExpect(jsonPath("$.[0].user.last_name").value("Hogwarts"))
                .andExpect(jsonPath("$.[0].user.email").value("r.h@gmail.com"))
                .andExpect(jsonPath("$.[0].user.teams").isArray())
                .andExpect(jsonPath("$.[0].user.allergies").isArray())
                .andExpect(jsonPath("$.[0].user.created_at").value(DATE.atOffset(ZoneOffset.UTC).toString()))
                .andExpect(jsonPath("$.[0].user.updated_at").value(DATE.atOffset(ZoneOffset.UTC).toString()))
                .andExpect(jsonPath("$.[0].snack.id").value(1))
                .andExpect(jsonPath("$.[0].snack.name").value("Rice Crispie Treat"))
                .andExpect(jsonPath("$.[0].snack.sweet").value("true"))
                .andExpect(jsonPath("$.[0].snack.savory").value("false"))
                .andExpect(jsonPath("$.[0].snack.difficulty").value(2))
                .andExpect(jsonPath("$.[0].snack.ingredients").isNotEmpty())
                .andExpect(jsonPath("$.[0].snack.created_at").value(DATE.atOffset(ZoneOffset.UTC).toString()))
                .andExpect(jsonPath("$.[0].snack.updated_at").value(DATE.atOffset(ZoneOffset.UTC).toString()));
    }

    @Test
    void test_updateUserSnackRankings_nullBody() throws Exception {

        //--------------------------------------------------
        // EXECUTE & VERIFY RESULTS
        mockMvc.perform(put("/snack-rankings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null)) // this is the test
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString())); // print the response
    }

    @Test
    void test_updateUserSnackRankings_conflict() throws Exception {
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
        team.setId(1L);
        team.setCreatedAt(DATE);
        team.setUpdatedAt(DATE);
        Team[] teams = { team };

        Ingredient ingredient = new Ingredient("Pecan");
        ingredient.setId(1L);
        ingredient.setCreatedAt(DATE);
        ingredient.setUpdatedAt(DATE);
        Ingredient[] allergies = { ingredient };

        User user = new User("Roger", "Hogwarts", "r.h@gmail.com", teams, allergies);
        user.setId(1L);
        user.setCreatedAt(DATE);
        user.setUpdatedAt(DATE);
        System.out.println("User = " + user);

        Ingredient ingredient1 = makeIngredient("Rice Crispy Cereal", 4L);
        Ingredient ingredient2 = makeIngredient("Margarine", 5L);
        Ingredient ingredient3 = makeIngredient("Marshmallow", 6L);
        Ingredient ingredient4 = makeIngredient("Vanilla", 7L);
        Ingredient[] ingredients = { ingredient1, ingredient2, ingredient3, ingredient4 };
        Snack snack = new Snack(
                "Rice Crispie Treat",
                true,
                false,
                2,
                ingredients
        );
        snack.setId(1L);
        snack.setCreatedAt(DATE);
        snack.setUpdatedAt(DATE);
        System.out.println("Snack = " + snack);

        // create the one to send in the request
        UserSnackRanking ranking = new UserSnackRanking(snack, user, SnackRank.RANK_10);
        ranking.setCreatedAt(DATE);
        ranking.setUpdatedAt(DATE);
        System.out.println("Ranking = " + ranking);

        //--------------------------------------------------
        // CONFIGURE MOCKS
        when(userSnackRankingRepo.saveAllAndFlush(any())).thenThrow(new OptimisticLockingFailureException("conflict"));

        //--------------------------------------------------
        // EXECUTE & VERIFY RESULTS
        mockMvc.perform(put("/snack-rankings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(ranking)))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString())); // print the response
    }

    @Test
    void test_updateUserSnackRankings_error() throws Exception {
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
        team.setId(1L);
        team.setCreatedAt(DATE);
        team.setUpdatedAt(DATE);
        Team[] teams = { team };

        Ingredient ingredient = new Ingredient("Pecan");
        ingredient.setId(1L);
        ingredient.setCreatedAt(DATE);
        ingredient.setUpdatedAt(DATE);
        Ingredient[] allergies = { ingredient };

        User user = new User("Roger", "Hogwarts", "r.h@gmail.com", teams, allergies);
        user.setId(1L);
        user.setCreatedAt(DATE);
        user.setUpdatedAt(DATE);
        System.out.println("User = " + user);

        Ingredient ingredient1 = makeIngredient("Rice Crispy Cereal", 4L);
        Ingredient ingredient2 = makeIngredient("Margarine", 5L);
        Ingredient ingredient3 = makeIngredient("Marshmallow", 6L);
        Ingredient ingredient4 = makeIngredient("Vanilla", 7L);
        Ingredient[] ingredients = { ingredient1, ingredient2, ingredient3, ingredient4 };
        Snack snack = new Snack(
                "Rice Crispie Treat",
                true,
                false,
                2,
                ingredients
        );
        snack.setId(1L);
        snack.setCreatedAt(DATE);
        snack.setUpdatedAt(DATE);
        System.out.println("Snack = " + snack);

        // create the one to send in the request
        UserSnackRanking ranking = new UserSnackRanking(snack, user, SnackRank.RANK_10);
        ranking.setCreatedAt(DATE);
        ranking.setUpdatedAt(DATE);
        System.out.println("Ranking = " + ranking);

        //--------------------------------------------------
        // CONFIGURE MOCKS
        when(userSnackRankingRepo.saveAllAndFlush(any())).thenThrow(new IllegalArgumentException("error"));

        //--------------------------------------------------
        // EXECUTE & VERIFY RESULTS
        mockMvc.perform(put("/snack-rankings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(ranking)))
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
    Ingredient makeIngredient(String name, Long id) {
        Ingredient ingredient = new Ingredient(name);
        ingredient.setId(id);
        ingredient.setCreatedAt(DATE);
        ingredient.setUpdatedAt(DATE);
        return ingredient;
    }
}
