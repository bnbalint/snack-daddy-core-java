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
public class TeamTest {

    @Autowired
    private JacksonTester<Team> jsonTester;

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
                "logo.com"
        );
        team.setId(1);
        team.setCreatedAt(DATE);
        team.setUpdatedAt(DATE);
        System.out.println("Team = " + team);

        //--------------------------------------------------
        // EXECUTE
        JsonContent<Team> json = jsonTester.write(team);
        System.out.println("json = " + json);

        //--------------------------------------------------
        // VERIFY RESULTS
        assertThat(json).isEqualToJson("team.json");
        assertThat(json).hasJsonPathNumberValue("$.id");
        assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo("Mules");
        assertThat(json).extractingJsonPathStringValue("$.rink").isEqualTo("BAIREL");
        assertThat(json).extractingJsonPathStringValue("$.level").isEqualTo("D5");
        assertThat(json).extractingJsonPathStringValue("$.primary_color").isEqualTo("#b88907");
        assertThat(json).extractingJsonPathStringValue("$.secondary_color").isEqualTo("#000000");
        assertThat(json).extractingJsonPathStringValue("$.ternary_color").isEqualTo("#c42323");
        assertThat(json).extractingJsonPathStringValue("$.logo_url").isEqualTo("logo.com");
        assertThat(json).extractingJsonPathStringValue("$.created_at").isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());
        assertThat(json).extractingJsonPathStringValue("$.updated_at").isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());
    }


    @Test
    void test_deserialization() throws Exception {

        //--------------------------------------------------
        // SET VALUES
        Team team = jsonTester.read("team.json").getObject();
        System.out.println("team = " + team);

        //--------------------------------------------------
        // VERIFY RESULTS
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
    }
}
