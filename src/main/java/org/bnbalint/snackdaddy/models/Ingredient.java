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
    private Instant createdAt;

    @Column(insertable = false, updatable = false)
    @UpdateTimestamp
    private Instant updatedAt;

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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }


    //-------------------------------------------
    // Functions
    //

    @Override
    public String toString() {
        return String.format("Ingredient(id=%d, name=%s, created=%s, updated=%s", id, name, createdAt, updatedAt);
    }
}
