package org.bnbalint.snackdaddy.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, updatable = false)
    private long id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String email;

    @ManyToMany
    @JoinTable(name = "team_membership", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "team_id"))
    private Team[] teams;

    @ManyToMany
    @JoinTable(name = "user_allergies", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "ingredient_id"))
    private Ingredient[] allergies;

    @Column(insertable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(insertable = false, updatable = false)
    @UpdateTimestamp
    private Instant updatedAt;


    //-------------------------------------------
    // Constructors
    //
    public User() {}

    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }


    public User(String firstName, String lastName, String email, Team[] teams, Ingredient[] allergies) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.teams = teams;
        this.allergies = allergies;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Team[] getTeams() {
        return teams;
    }

    public void setTeams(Team[] teams) {
        this.teams = teams;
    }

    public Ingredient[] getAllergies() {
        return allergies;
    }

    public void setAllergies(Ingredient[] allergies) {
        this.allergies = allergies;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    //-------------------------------------------
    // Functions
    //

    @Override
    public String toString() {
        return String.format("User(id=%d, firstName=%s, lastName=%s, email=%s, teams=[ %s ], allergies=[ %s ], created=%s, updated=%s", id, firstName, lastName, email, Arrays.toString(teams), Arrays.toString(allergies), createdAt, updatedAt);
    }



}
