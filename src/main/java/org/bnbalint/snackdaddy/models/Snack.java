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
@Table(name = "snacks")
public class Snack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, updatable = false)
    private long id;

    @Column
    private String name;

    @Column
    private Boolean sweet;

    @Column
    private Boolean savory;

    @Column
    private int difficulty;

    @Column
    private String recipeUrl;

    @ManyToMany
    @JoinTable(name = "snack_ingredients", joinColumns = @JoinColumn(name = "snack_id"), inverseJoinColumns = @JoinColumn(name = "ingredient_id"))
    private Ingredient[] ingredients;


    @Column(insertable = false, updatable = false)
    @CreationTimestamp
    private Instant created;

    @Column(insertable = false, updatable = false)
    @UpdateTimestamp
    private Instant updated;


    //-------------------------------------------
    // Constructors
    //
    public Snack() {
        this.recipeUrl = ""; // this can always be blank, no need to be null
    }

    /**
     * @param name - the name of the snack
     * @param sweet - true/false on whether the snack is sweet
     * @param savory = true/false on whether the snack is savory
     * @param difficulty - the difficulty rating (1-10, inclusive)
     * @param recipeUrl - the url for the recipe
     */
    public Snack(String name, Boolean sweet, Boolean savory, int difficulty, Ingredient[] ingredients, String recipeUrl) {
        this.name = name;
        this.sweet = sweet;
        this.savory = savory;
        this.difficulty = difficulty;
        this.ingredients = ingredients;
        this.recipeUrl = recipeUrl;
    }

    /**
     * @param name - the name of the snack
     * @param sweet - true/false on whether the snack is sweet
     * @param savory = true/false on whether the snack is savory
     * @param difficulty - the difficulty rating (1-10, inclusive)
     * recipeUrl is set to ""
     */
    public Snack(String name, Boolean sweet, Boolean savory, int difficulty, Ingredient[] ingredients) {
        this.name = name;
        this.sweet = sweet;
        this.savory = savory;
        this.difficulty = difficulty;
        this.ingredients = ingredients;
        this.recipeUrl = "";
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

    public Boolean getSweet() {
        return sweet;
    }

    public void setSweet(Boolean sweet) {
        this.sweet = sweet;
    }

    public Boolean getSavory() {
        return savory;
    }

    public void setSavory(Boolean savory) {
        this.savory = savory;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getRecipeUrl() {
        return recipeUrl;
    }

    public void setRecipeUrl(String recipeUrl) {
        this.recipeUrl = recipeUrl;
    }

    public Ingredient[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(Ingredient[] ingredients) {
        this.ingredients = ingredients;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated (Instant created) {
        this.created = created;
    }

    public Instant getUpdated() {
        return updated;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }


    //-------------------------------------------
    // Functions
    //

    @Override
    public String toString() {
        return String.format("Snack(id=%d, name=%s, sweet=%s, savory=%s, difficulty=%d, recipeUrl=%s, ingredients=[ %s ], created=%s, updated=%s", id, name, sweet, savory, difficulty, recipeUrl, ingredients, created, updated);
    }
}
