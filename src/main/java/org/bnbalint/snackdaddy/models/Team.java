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
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, updatable = false)
    private long id;

    @Column
    private String name;

    @Column
    @Enumerated(EnumType.STRING)
    private Rink rink;

    @Column
    @Enumerated(EnumType.STRING)
    private Level level;

    @Column
    private String primaryColor;

    @Column
    private String secondaryColor;

    @Column
    private String ternaryColor;

    @Column
    private String logoUrl;

    @Column(insertable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(insertable = false, updatable = false)
    @UpdateTimestamp
    private Instant updatedAt;



    //-------------------------------------------
    // Constructors
    //
    public Team() {}

    public Team(String name, Rink rink, Level level, String primaryColor, String secondaryColor, String ternaryColor, String logoUrl) {
        this.name = name;
        this.rink = rink;
        this.level = level;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.ternaryColor = ternaryColor;
        this.logoUrl = logoUrl;
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

    public Rink getRink() {
        return rink;
    }

    public void setRink(Rink rink) {
        this.rink = rink;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public String getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
    }

    public String getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(String secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public String getTernaryColor() {
        return ternaryColor;
    }

    public void setTernaryColor(String ternaryColor) {
        this.ternaryColor = ternaryColor;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
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
        return String.format("Team(id=%d, name=%s, rink=%s, level=%s, primaryColor=%s, secondaryColor=%s, ternaryColor=%s, logoUrl=%s, created=%s, updated=%s", id, name, rink, level, primaryColor, secondaryColor, ternaryColor, logoUrl, createdAt, updatedAt);
    }
}
