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
public class SuggestedAllergyTest {

    @Autowired
    private JacksonTester<SuggestedAllergy> jsonTester;

    static Instant DATE = Instant.parse("2026-07-01T00:00:01Z");

    @Test
    void test_serialization() throws Exception {

        //--------------------------------------------------
        // SET VALUES
        SuggestedAllergy suggestion = new SuggestedAllergy("Pine nut");
        suggestion.setId(1);
        suggestion.setCreatedAt(DATE);
        suggestion.setUpdatedAt(DATE);
        System.out.println("SuggestedAllergy = " + suggestion);

        //--------------------------------------------------
        // EXECUTE
        JsonContent<SuggestedAllergy> json = jsonTester.write(suggestion);
        System.out.println("json = " + json);

        //--------------------------------------------------
        // VERIFY RESULTS
        assertThat(json).isEqualToJson("suggestedAllergy.json");
        assertThat(json).hasJsonPathNumberValue("$.id");
        assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo("Pine nut");
        assertThat(json).extractingJsonPathStringValue("$.created_at").isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());
        assertThat(json).extractingJsonPathStringValue("$.updated_at").isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());
    }


    @Test
    void test_deserialization() throws Exception {

        //--------------------------------------------------
        // SET VALUES
        SuggestedAllergy suggestion = jsonTester.read("suggestedAllergy.json").getObject();
        System.out.println("suggestion = " + suggestion);

        //--------------------------------------------------
        // VERIFY RESULTS
        assertThat(suggestion.getId()).isEqualTo(1);
        assertThat(suggestion.getName()).isEqualTo("Pine nut");
        assertThat(suggestion.getCreatedAt()).isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());
        assertThat(suggestion.getUpdatedAt()).isEqualTo(DATE.atOffset(ZoneOffset.UTC).toString());
    }
}
