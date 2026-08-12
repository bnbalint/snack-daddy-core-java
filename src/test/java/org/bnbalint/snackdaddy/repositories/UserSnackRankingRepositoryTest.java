package org.bnbalint.snackdaddy.repositories;

import org.bnbalint.snackdaddy.models.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class UserSnackRankingRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserSnackRankingRepository userSnackRankingRepo;


    @Test
    void test_findAll() {

        //--------------------------------------------------
        // SET VALUES
        User user = new User("Roger", "Hogwarts", "RankingTest@gmail.com");
        Ingredient[] ingredients = { };
        Snack snack = new Snack("Ranking Test", true, false, 2, ingredients);

        UserSnackRanking ranking = new UserSnackRanking(snack, user, SnackRank.RANK_10);


        //--------------------------------------------------
        // CONFIGURE MOCKS
        // Persist the data to the database (first the user, and snack, then the ranking)
        entityManager.persist(user);
        entityManager.persistAndFlush(snack);
        entityManager.persistAndFlush(ranking);
        System.out.println("Saved ranking = " + ranking);

        //--------------------------------------------------
        // EXECUTE
        var foundRankings = userSnackRankingRepo.findAll();
        System.out.println("Found rankings = " + foundRankings);

        //--------------------------------------------------
        // VERIFY RESULTS
        assertEquals(1, foundRankings.size());
        var firstRanking = foundRankings.get(0);
        assertEquals(SnackRank.RANK_10, firstRanking.getRank());

        var foundUser = firstRanking.getUser();
        assertEquals("Roger", foundUser.getFirstName());
        assertEquals("Hogwarts", foundUser.getLastName());
        assertEquals("RankingTest@gmail.com", foundUser.getEmail());

        var foundSnack = firstRanking.getSnack();
        assertEquals("Ranking Test", foundSnack.getName());
        assertEquals(true, foundSnack.getSweet());
        assertEquals(false, foundSnack.getSavory());
        assertEquals(2, foundSnack.getDifficulty());
    }


    @Test
    void test_saveAndFlush() {

        //--------------------------------------------------
        // SET VALUES
        User user = new User("Roger", "Hogwarts", "RankingTest2@gmail.com");
        Ingredient[] ingredients = { };
        Snack snack = new Snack("Ranking Test2", true, false, 2, ingredients);

        UserSnackRanking ranking = new UserSnackRanking(snack, user, SnackRank.RANK_1);


        //--------------------------------------------------
        // EXECUTE
        var savedRanking = userSnackRankingRepo.saveAndFlush(ranking);
        System.out.println("Saved ranking = " + savedRanking);

        //--------------------------------------------------
        // VERIFY RESULTS
        assertEquals(SnackRank.RANK_1, savedRanking.getRank());
        assertNotNull(savedRanking.getCreatedAt());
        assertNotNull(savedRanking.getUpdatedAt());

        assertEquals("Roger", savedRanking.getUser().getFirstName());
        assertEquals("Hogwarts", savedRanking.getUser().getLastName());
        assertEquals("RankingTest2@gmail.com", savedRanking.getUser().getEmail());

        assertEquals("Ranking Test2", savedRanking.getSnack().getName());
        assertEquals(true, savedRanking.getSnack().getSweet());
        assertEquals(false, savedRanking.getSnack().getSavory());
        assertEquals(2, savedRanking.getSnack().getDifficulty());
    }


    @Test
    void test_findAllByUserId() {

        //--------------------------------------------------
        // SET VALUES
        User user = new User("Roger", "Hogwarts", "RankingTest@gmail.com");
        Ingredient[] ingredients = { };
        Snack snack = new Snack("Ranking Test", true, false, 2, ingredients);

        UserSnackRanking ranking = new UserSnackRanking(snack, user, SnackRank.RANK_10);


        //--------------------------------------------------
        // CONFIGURE MOCKS
        // Persist the data to the database (first the user, and snack, then the ranking)
        entityManager.persist(user);
        entityManager.persistAndFlush(snack);
        entityManager.persistAndFlush(ranking);
        System.out.println("Saved ranking = " + ranking);
        var savedUserId = ranking.getUser().getId();

        //--------------------------------------------------
        // EXECUTE
        var foundRankings = userSnackRankingRepo.findAllByUserId(savedUserId);
        System.out.println("Found rankings = " + foundRankings);

        //--------------------------------------------------
        // VERIFY RESULTS
        assertEquals(1, foundRankings.size());
        var firstRanking = foundRankings.get(0);
        assertEquals(SnackRank.RANK_10, firstRanking.getRank());

        var foundUser = firstRanking.getUser();
        assertEquals("Roger", foundUser.getFirstName());
        assertEquals("Hogwarts", foundUser.getLastName());
        assertEquals("RankingTest@gmail.com", foundUser.getEmail());

        var foundSnack = firstRanking.getSnack();
        assertEquals("Ranking Test", foundSnack.getName());
        assertEquals(true, foundSnack.getSweet());
        assertEquals(false, foundSnack.getSavory());
        assertEquals(2, foundSnack.getDifficulty());
    }


    @Test
    void test_saveAllAndFlush() {

        //--------------------------------------------------
        // SET VALUES
        User user = new User("Roger", "Hogwarts", "SaveAllRankingTest@gmail.com");
        Ingredient[] ingredients = { };
        Snack snack1 = new Snack("Ranking Test Snack1", true, false, 2, ingredients);
        Snack snack2 = new Snack("Ranking Test Snack2", true, false, 2, ingredients);

        UserSnackRanking ranking1 = new UserSnackRanking(snack1, user, SnackRank.RANK_1);
        UserSnackRanking ranking2 = new UserSnackRanking(snack2, user, SnackRank.RANK_2);



        //--------------------------------------------------
        // EXECUTE
        var savedRankings = userSnackRankingRepo.saveAllAndFlush(List.of(ranking1, ranking2));
        System.out.println("Saved rankings = " + savedRankings);

        //--------------------------------------------------
        // VERIFY RESULTS
        assertEquals(2, savedRankings.size());

        var firstSaved = savedRankings.get(0);
        assertEquals(SnackRank.RANK_1, firstSaved.getRank());
        assertNotNull(firstSaved.getCreatedAt());
        assertNotNull(firstSaved.getUpdatedAt());

        assertEquals("Roger", firstSaved.getUser().getFirstName());
        assertEquals("Hogwarts", firstSaved.getUser().getLastName());
        assertEquals("SaveAllRankingTest@gmail.com", firstSaved.getUser().getEmail());

        assertEquals("Ranking Test Snack1", firstSaved.getSnack().getName());
        assertEquals(true, firstSaved.getSnack().getSweet());
        assertEquals(false, firstSaved.getSnack().getSavory());
        assertEquals(2, firstSaved.getSnack().getDifficulty());


        var secondSaved = savedRankings.get(1);
        assertEquals(SnackRank.RANK_2, secondSaved.getRank());
        assertNotNull(secondSaved.getCreatedAt());
        assertNotNull(secondSaved.getUpdatedAt());

        assertEquals("Roger", secondSaved.getUser().getFirstName());
        assertEquals("Hogwarts", secondSaved.getUser().getLastName());
        assertEquals("SaveAllRankingTest@gmail.com", secondSaved.getUser().getEmail());

        assertEquals("Ranking Test Snack2", secondSaved.getSnack().getName());
        assertEquals(true, secondSaved.getSnack().getSweet());
        assertEquals(false, secondSaved.getSnack().getSavory());
        assertEquals(2, secondSaved.getSnack().getDifficulty());
    }
}
