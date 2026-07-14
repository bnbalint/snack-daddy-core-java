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
public class UserTest {

    @Autowired
    private JacksonTester<User> jsonTester;

    static Instant DATE = Instant.parse("2026-07-01T00:00:01Z");

    @Test
    void test_serialization() throws Exception {

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
        // EXECUTE
        JsonContent<User> json = jsonTester.write(user);
        System.out.println("json = " + json);

        //--------------------------------------------------
        // VERIFY RESULTS
        assertThat(json).isEqualToJson("user.json");
        assertThat(json).hasJsonPathNumberValue("$.id");
        assertThat(json).extractingJsonPathStringValue("$.first_name").isEqualTo("Roger");
        assertThat(json).extractingJsonPathStringValue("$.last_name").isEqualTo("Hogwarts");
        assertThat(json).extractingJsonPathStringValue("$.email").isEqualTo("r.h@gmail.com");
        assertThat(json).extractingJsonPathArrayValue("$.teams").hasSize(1);
        assertThat(json).extractingJsonPathArrayValue("$.allergies").hasSize(1);
        assertThat(json).extractingJsonPathStringValue("$.created").isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());
        assertThat(json).extractingJsonPathStringValue("$.updated").isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());
    }


    @Test
    void test_deserialization() throws Exception {

        //--------------------------------------------------
        // SET VALUES
        User user = jsonTester.read("user.json").getObject();
        System.out.println("user = " + user);

        //--------------------------------------------------
        // VERIFY RESULTS
        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getFirstName()).isEqualTo("Roger");
        assertThat(user.getLastName()).isEqualTo("Hogwarts");
        assertThat(user.getEmail()).isEqualTo("r.h@gmail.com");

        assertThat(user.getTeams()).hasSize(1);
        var team = user.getTeams()[0];
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

        assertThat(user.getAllergies()).hasSize(1);
        var allergy = user.getAllergies()[0];
        assertThat(allergy.getId()).isEqualTo(1);
        assertThat(allergy.getName()).isEqualTo("Pecan");
        assertThat(allergy.getCreatedAt()).isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());
        assertThat(allergy.getUpdatedAt()).isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());

        assertThat(user.getCreatedAt()).isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());
        assertThat(user.getUpdatedAt()).isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());
    }
}
