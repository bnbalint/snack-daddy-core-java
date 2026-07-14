package org.bnbalint.snackdaddy.models;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class SnackLogTest {

    @Autowired
    private JacksonTester<SnackLog> jsonTester;

    static Instant DATE = Instant.parse("2026-07-01T00:00:01Z");

    @Test
    void test_serialization() throws Exception {

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
        // EXECUTE
        JsonContent<SnackLog> json = jsonTester.write(snackLogEntry);
        System.out.println("json = " + json);

        //--------------------------------------------------
        // VERIFY RESULTS
        assertThat(json).isEqualToJson("snackLog.json");
        assertThat(json).hasJsonPathNumberValue("$.id");
        assertThat(json).extractingJsonPathStringValue("$.snack.name").isEqualTo("Rice Crispie Treat");
        assertThat(json).extractingJsonPathBooleanValue("$.snack.sweet").isEqualTo(true);
        assertThat(json).extractingJsonPathBooleanValue("$.snack.savory").isEqualTo(false);
        assertThat(json).extractingJsonPathNumberValue("$.snack.difficulty").isEqualTo(2);
        assertThat(json).extractingJsonPathStringValue("$.snack.created_at").isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());
        assertThat(json).extractingJsonPathStringValue("$.snack.updated_at").isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());
        assertThat(json).extractingJsonPathArrayValue("$.snack.ingredients").hasSize(4);

        assertThat(json).extractingJsonPathStringValue("$.team.name").isEqualTo("Mules");
        assertThat(json).extractingJsonPathStringValue("$.team.rink").isEqualTo("BAIREL");
        assertThat(json).extractingJsonPathStringValue("$.team.level").isEqualTo("D5");
        assertThat(json).extractingJsonPathStringValue("$.team.primary_color").isEqualTo("#b88907");
        assertThat(json).extractingJsonPathStringValue("$.team.secondary_color").isEqualTo("#000000");
        assertThat(json).extractingJsonPathStringValue("$.team.ternary_color").isEqualTo("#c42323");
        assertThat(json).extractingJsonPathStringValue("$.team.logo_url").isEqualTo("logo.com");

        assertThat(json).extractingJsonPathStringValue("$.date_made").isEqualTo("2026-06-01");
        assertThat(json).extractingJsonPathStringValue("$.created_at").isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());
        assertThat(json).extractingJsonPathStringValue("$.updated_at").isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());
    }


    @Test
    void test_deserialization() throws Exception {

        //--------------------------------------------------
        // SET VALUES
        SnackLog snackLogEntry = jsonTester.read("snackLog.json").getObject();
        System.out.println("snackLogEntry = " + snackLogEntry);

        //--------------------------------------------------
        // VERIFY RESULTS
        assertThat(snackLogEntry.getId()).isEqualTo(1);

        var snack = snackLogEntry.getSnack();
        assertThat(snack.getName()).isEqualTo("Rice Crispie Treat");
        assertThat(snack.getSweet()).isEqualTo(true);
        assertThat(snack.getSavory()).isEqualTo(false);
        assertThat(snack.getDifficulty()).isEqualTo(2);
        assertThat(snack.getIngredients()).hasSize(4);
        assertThat(snack.getCreatedAt()).isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());
        assertThat(snack.getUpdatedAt()).isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());

        var team = snackLogEntry.getTeam();
        assertThat(team.getId()).isEqualTo(1);
        assertThat(team.getName()).isEqualTo("Mules");
        assertThat(team.getRink()).isEqualTo(Rink.BAIREL);
        assertThat(team.getLevel()).isEqualTo(Level.D5);
        assertThat(team.getPrimaryColor()).isEqualTo("#b88907");
        assertThat(team.getSecondaryColor()).isEqualTo("#000000");
        assertThat(team.getTernaryColor()).isEqualTo("#c42323");
        assertThat(team.getLogoUrl()).isEqualTo("logo.com");
        assertThat(team.getCreatedAt()).isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());
        assertThat(team.getUpdatedAt()).isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());

        assertThat(snackLogEntry.getDateMade()).isEqualTo("2026-06-01");
        assertThat(snackLogEntry.getCreatedAt()).isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());
        assertThat(snackLogEntry.getUpdatedAt()).isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());
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
