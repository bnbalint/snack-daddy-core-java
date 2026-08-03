package org.bnbalint.snackdaddy.models;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.Instant;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UserSnackRankingTest {

    @Autowired
    private JacksonTester<UserSnackRanking> jsonTester;

    static Instant DATE = Instant.parse("2026-07-01T00:00:01Z");

    @Test
    void test_serialization_primaryConstructor() throws Exception {

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


        UserSnackRanking ranking = new UserSnackRanking();
        ranking.setSnack(snack);
        ranking.setUser(user);
        ranking.setRank(SnackRank.RANK_10);
        ranking.setCreatedAt(DATE);
        ranking.setUpdatedAt(DATE);
        System.out.println("SnackRanking = " + ranking);

        //--------------------------------------------------
        // EXECUTE
        JsonContent<UserSnackRanking> json = jsonTester.write(ranking);
        System.out.println("json = " + json);

        //--------------------------------------------------
        // VERIFY RESULTS
        assertThat(json).isEqualToJson("userSnackRanking.json");
        assertThat(json).extractingJsonPathStringValue("$.rank").isEqualTo("RANK_10");
        assertThat(json).extractingJsonPathStringValue("$.created_at").isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());
        assertThat(json).extractingJsonPathStringValue("$.updated_at").isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());

        assertThat(json).hasJsonPathNumberValue("$.user.id");
        assertThat(json).extractingJsonPathStringValue("$.user.first_name").isEqualTo("Roger");
        assertThat(json).extractingJsonPathStringValue("$.user.last_name").isEqualTo("Hogwarts");
        assertThat(json).extractingJsonPathStringValue("$.user.email").isEqualTo("r.h@gmail.com");
        assertThat(json).extractingJsonPathArrayValue("$.user.teams").hasSize(1);
        assertThat(json).extractingJsonPathArrayValue("$.user.allergies").hasSize(1);
        assertThat(json).extractingJsonPathStringValue("$.user.created_at").isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());
        assertThat(json).extractingJsonPathStringValue("$.user.updated_at").isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());

        assertThat(json).extractingJsonPathStringValue("$.snack.name").isEqualTo("Rice Crispie Treat");
        assertThat(json).extractingJsonPathBooleanValue("$.snack.sweet").isEqualTo(true);
        assertThat(json).extractingJsonPathBooleanValue("$.snack.savory").isEqualTo(false);
        assertThat(json).extractingJsonPathNumberValue("$.snack.difficulty").isEqualTo(2);
        assertThat(json).extractingJsonPathStringValue("$.snack.created_at").isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());
        assertThat(json).extractingJsonPathStringValue("$.snack.updated_at").isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());
        assertThat(json).extractingJsonPathArrayValue("$.snack.ingredients").hasSize(4);
    }

    @Test
    void test_serialization_secondConstructor() throws Exception {

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


        UserSnackRanking ranking = new UserSnackRanking(snack, user);
        ranking.setRank(SnackRank.RANK_10);
        ranking.setCreatedAt(DATE);
        ranking.setUpdatedAt(DATE);
        System.out.println("SnackRanking = " + ranking);

        //--------------------------------------------------
        // EXECUTE
        JsonContent<UserSnackRanking> json = jsonTester.write(ranking);
        System.out.println("json = " + json);

        //--------------------------------------------------
        // VERIFY RESULTS
        assertThat(json).isEqualToJson("userSnackRanking.json");
        assertThat(json).extractingJsonPathStringValue("$.rank").isEqualTo("RANK_10");
        assertThat(json).extractingJsonPathStringValue("$.created_at").isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());
        assertThat(json).extractingJsonPathStringValue("$.updated_at").isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());

        assertThat(json).hasJsonPathNumberValue("$.user.id");
        assertThat(json).extractingJsonPathStringValue("$.user.first_name").isEqualTo("Roger");
        assertThat(json).extractingJsonPathStringValue("$.user.last_name").isEqualTo("Hogwarts");
        assertThat(json).extractingJsonPathStringValue("$.user.email").isEqualTo("r.h@gmail.com");
        assertThat(json).extractingJsonPathArrayValue("$.user.teams").hasSize(1);
        assertThat(json).extractingJsonPathArrayValue("$.user.allergies").hasSize(1);
        assertThat(json).extractingJsonPathStringValue("$.user.created_at").isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());
        assertThat(json).extractingJsonPathStringValue("$.user.updated_at").isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());

        assertThat(json).extractingJsonPathStringValue("$.snack.name").isEqualTo("Rice Crispie Treat");
        assertThat(json).extractingJsonPathBooleanValue("$.snack.sweet").isEqualTo(true);
        assertThat(json).extractingJsonPathBooleanValue("$.snack.savory").isEqualTo(false);
        assertThat(json).extractingJsonPathNumberValue("$.snack.difficulty").isEqualTo(2);
        assertThat(json).extractingJsonPathStringValue("$.snack.created_at").isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());
        assertThat(json).extractingJsonPathStringValue("$.snack.updated_at").isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());
        assertThat(json).extractingJsonPathArrayValue("$.snack.ingredients").hasSize(4);
    }

    @Test
    void test_serialization_thirdConstructor() throws Exception {

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


        UserSnackRanking ranking = new UserSnackRanking(snack, user, SnackRank.RANK_10);
        ranking.setCreatedAt(DATE);
        ranking.setUpdatedAt(DATE);
        System.out.println("SnackRanking = " + ranking);


        //--------------------------------------------------
        // EXECUTE
        JsonContent<UserSnackRanking> json = jsonTester.write(ranking);
        System.out.println("json = " + json);

        //--------------------------------------------------
        // VERIFY RESULTS
        assertThat(json).isEqualToJson("userSnackRanking.json");
        assertThat(json).extractingJsonPathStringValue("$.rank").isEqualTo("RANK_10");
        assertThat(json).extractingJsonPathStringValue("$.created_at").isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());
        assertThat(json).extractingJsonPathStringValue("$.updated_at").isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());

        assertThat(json).hasJsonPathNumberValue("$.user.id");
        assertThat(json).extractingJsonPathStringValue("$.user.first_name").isEqualTo("Roger");
        assertThat(json).extractingJsonPathStringValue("$.user.last_name").isEqualTo("Hogwarts");
        assertThat(json).extractingJsonPathStringValue("$.user.email").isEqualTo("r.h@gmail.com");
        assertThat(json).extractingJsonPathArrayValue("$.user.teams").hasSize(1);
        assertThat(json).extractingJsonPathArrayValue("$.user.allergies").hasSize(1);
        assertThat(json).extractingJsonPathStringValue("$.user.created_at").isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());
        assertThat(json).extractingJsonPathStringValue("$.user.updated_at").isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());

        assertThat(json).extractingJsonPathStringValue("$.snack.name").isEqualTo("Rice Crispie Treat");
        assertThat(json).extractingJsonPathBooleanValue("$.snack.sweet").isEqualTo(true);
        assertThat(json).extractingJsonPathBooleanValue("$.snack.savory").isEqualTo(false);
        assertThat(json).extractingJsonPathNumberValue("$.snack.difficulty").isEqualTo(2);
        assertThat(json).extractingJsonPathStringValue("$.snack.created_at").isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());
        assertThat(json).extractingJsonPathStringValue("$.snack.updated_at").isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());
        assertThat(json).extractingJsonPathArrayValue("$.snack.ingredients").hasSize(4);
    }


    @Test
    void test_deserialization() throws Exception {

        //--------------------------------------------------
        // SET VALUES
        UserSnackRanking ranking = jsonTester.read("userSnackRanking.json").getObject();
        System.out.println("ranking = " + ranking);

        //--------------------------------------------------
        // VERIFY RESULTS
        assertThat(ranking.getRank()).isEqualTo(SnackRank.RANK_10);
        assertThat(ranking.getCreatedAt()).isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());
        assertThat(ranking.getUpdatedAt()).isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());

        assertThat(ranking.getUser().getId()).isEqualTo(1);
        assertThat(ranking.getUser().getFirstName()).isEqualTo("Roger");
        assertThat(ranking.getUser().getLastName()).isEqualTo("Hogwarts");
        assertThat(ranking.getUser().getEmail()).isEqualTo("r.h@gmail.com");

        assertThat(ranking.getUser().getTeams()).hasSize(1);
        var team = ranking.getUser().getTeams()[0];
        assertThat(team.getId()).isEqualTo(1);
        assertThat(team.getName()).isEqualTo("Mules");
        assertThat(team.getRink()).isEqualTo(Rink.BAIREL);
        assertThat(team.getLevel()).isEqualTo(Level.D5);
        assertThat(team.getPrimaryColor()).isEqualTo("#b88907");
        assertThat(team.getSecondaryColor()).isEqualTo("#000000");
        assertThat(team.getTernaryColor()).isEqualTo("#c42323");
        assertThat(team.getLogoUrl()).isEqualTo("");
        assertThat(team.getCreatedAt()).isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());
        assertThat(team.getUpdatedAt()).isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());

        assertThat(ranking.getUser().getAllergies()).hasSize(1);
        var allergy = ranking.getUser().getAllergies()[0];
        assertThat(allergy.getId()).isEqualTo(1);
        assertThat(allergy.getName()).isEqualTo("Pecan");
        assertThat(allergy.getCreatedAt()).isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());
        assertThat(allergy.getUpdatedAt()).isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());

        var snack = ranking.getSnack();
        assertThat(snack.getId()).isEqualTo(1);
        assertThat(snack.getName()).isEqualTo("Rice Crispie Treat");
        assertThat(snack.getSweet()).isEqualTo(true);
        assertThat(snack.getSavory()).isEqualTo(false);
        assertThat(snack.getDifficulty()).isEqualTo(2);
        assertThat(snack.getIngredients()).hasSize(4);
        assertThat(snack.getCreatedAt()).isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());
        assertThat(snack.getUpdatedAt()).isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());


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
