package org.bnbalint.snackdaddy.repositories;

import org.bnbalint.snackdaddy.models.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
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
}
