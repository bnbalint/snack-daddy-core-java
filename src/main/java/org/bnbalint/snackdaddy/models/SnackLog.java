package org.bnbalint.snackdaddy.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Entity
@Table(name = "snack_log")
public class SnackLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, updatable = false)
    private long id;

    @OneToOne
    @JoinColumn(name = "snack_id", referencedColumnName = "id")
    private Snack snack;

    @OneToOne
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    private Team team;

    @Column
    private LocalDate dateMade;

    @Column(insertable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(insertable = false, updatable = false)
    @UpdateTimestamp
    private Instant updatedAt;

    //-------------------------------------------
    // Constructors
    //
    public SnackLog() {}

    public SnackLog(Snack snack, Team team, LocalDate dateMade) {
        this.snack = snack;
        this.team = team;
        this.dateMade = dateMade;
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

    public Snack getSnack() {
        return this.snack;
    }

    public void setSnack(Snack snack) {
        this.snack = snack;
    }

    public Team getTeam() {
        return this.team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public LocalDate getDateMade() {
        return this.dateMade;
    }

    public void setDateMade(LocalDate dateMade) {
        this.dateMade = dateMade;
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
        return String.format("SnackLog(id=%d, snack=[ %s ], team=[ %s ], dateMade=%s, created=%s, updated=%s", id, snack, team, dateMade, createdAt, updatedAt);
    }

}
