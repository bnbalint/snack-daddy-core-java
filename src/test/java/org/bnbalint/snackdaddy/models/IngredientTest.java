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
public class IngredientTest {

    @Autowired
    private JacksonTester<Ingredient> jsonTester;

    static Instant DATE = Instant.parse("2026-07-01T00:00:01Z");

    @Test
    void test_serialization() throws Exception {

        //--------------------------------------------------
        // SET VALUES
        Ingredient ingredient = new Ingredient("Pecan");
        ingredient.setId(1);
        ingredient.setCreatedAt(DATE);
        ingredient.setUpdatedAt(DATE);
        System.out.println("Ingredient = " + ingredient);

        //--------------------------------------------------
        // EXECUTE
        JsonContent<Ingredient> json = jsonTester.write(ingredient);
        System.out.println("json = " + json);

        //--------------------------------------------------
        // VERIFY RESULTS
        assertThat(json).isEqualToJson("ingredient.json");
        assertThat(json).hasJsonPathNumberValue("$.id");
        assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo("Pecan");
        assertThat(json).extractingJsonPathStringValue("$.created").isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());
        assertThat(json).extractingJsonPathStringValue("$.updated").isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());
    }


    @Test
    void test_deserialization() throws Exception {

        //--------------------------------------------------
        // SET VALUES
        Ingredient ingredient = jsonTester.read("ingredient.json").getObject();
        System.out.println("ingredient = " + ingredient);

        //--------------------------------------------------
        // VERIFY RESULTS
        assertThat(ingredient.getId()).isEqualTo(1);
        assertThat(ingredient.getName()).isEqualTo("Pecan");
        assertThat(ingredient.getCreatedAt()).isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());
        assertThat(ingredient.getUpdatedAt()).isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());
    }
}
