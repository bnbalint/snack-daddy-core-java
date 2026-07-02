package org.bnbalint.snackdaddy.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Entity
@Table(name = "ingredients")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, updatable = false)
    private long id;

    @Column
    private String name;

    @Column(insertable = false, updatable = false)
    @CreationTimestamp
    private Instant created;

    @Column(insertable = false, updatable = false)
    @UpdateTimestamp
    private Instant updated;

    //-------------------------------------------
    // Constructors
    //
    public Ingredient() {}

    public Ingredient(String name) {
        this.name = name;
    }


    //-------------------------------------------
    // Getters and Setters
    //
    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getUpdated() {
        return this.updated;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }


    //-------------------------------------------
    // Functions
    //

    @Override
    public String toString() {
        return String.format("Ingredient(id=%d, name=%s, created=%s, updated=%s", id, name, created, updated);
    }
}
