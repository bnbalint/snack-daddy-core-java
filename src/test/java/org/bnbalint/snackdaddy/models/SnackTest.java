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
public class SnackTest {

    @Autowired
    private JacksonTester<Snack> jsonTester;

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
        snack.setCreated(DATE);
        snack.setUpdated(DATE);
        System.out.println("Snack = " + snack);

        //--------------------------------------------------
        // EXECUTE
        JsonContent<Snack> json = jsonTester.write(snack);
        System.out.println("json = " + json);

        //--------------------------------------------------
        // VERIFY RESULTS
        assertThat(json).isEqualToJson("snack.json");
        assertThat(json).hasJsonPathNumberValue("$.id");
        assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo("Rice Crispie Treat");
        assertThat(json).extractingJsonPathBooleanValue("$.sweet").isEqualTo(true);
        assertThat(json).extractingJsonPathBooleanValue("$.savory").isEqualTo(false);
        assertThat(json).extractingJsonPathNumberValue("$.difficulty").isEqualTo(2);
        assertThat(json).extractingJsonPathStringValue("$.created").isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());
        assertThat(json).extractingJsonPathStringValue("$.updated").isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());
        assertThat(json).extractingJsonPathArrayValue("$.ingredients").hasSize(4);
    }


    @Test
    void test_deserialization() throws Exception {

        //--------------------------------------------------
        // SET VALUES
        Snack snack = jsonTester.read("snack.json").getObject();
        System.out.println("snack = " + snack);

        //--------------------------------------------------
        // VERIFY RESULTS
        assertThat(snack.getId()).isEqualTo(1);
        assertThat(snack.getName()).isEqualTo("Rice Crispie Treat");
        assertThat(snack.getSweet()).isEqualTo(true);
        assertThat(snack.getSavory()).isEqualTo(false);
        assertThat(snack.getDifficulty()).isEqualTo(2);
        assertThat(snack.getIngredients()).hasSize(4);
        assertThat(snack.getCreated()).isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());
        assertThat(snack.getUpdated()).isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());
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
        ingredient.setCreated(DATE);
        ingredient.setUpdated(DATE);
        return ingredient;
    }
}
