package org.bnbalint.snackdaddy.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.Instant;
import java.util.Arrays;

/**
 * This table holds user's snack rankings
 * Each snack can only be rated once per user (PRIMARY KEY constraint on the snack_id, user_id pair)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Entity
@Table(name = "user_snack_rankings")
public class UserSnackRanking {

    // this is the composite ID of userId and snackID (managed by the special class below)
    @EmbeddedId
    private UserSnackRankingId id = new UserSnackRankingId();

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId("snackId") // keeps the EmbeddedId updated
    @JoinColumn(name = "snack_id", referencedColumnName = "id")
    private Snack snack;

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId("userId") // keeps the EmbeddedId updated
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column
    private SnackRank rank;

    @Column(insertable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(insertable = false, updatable = false)
    @UpdateTimestamp
    private Instant updatedAt;


    //-------------------------------------------
    // Constructors
    //
    public UserSnackRanking() {}

    public UserSnackRanking(Snack snack, User user, SnackRank rank) {
        this.snack = snack;
        this.user = user;
        this.rank = rank;
    }

    public UserSnackRanking(Snack snack, User user) {
        this.snack = snack;
        this.user = user;
        this.rank = SnackRank.UNRANKED;
    }

    //-------------------------------------------
    // Getters and Setters
    //
    public UserSnackRankingId getId() {
        return id;
    }

    public void setId(UserSnackRankingId id) {
        this.id = id;
    }

    public Snack getSnack() {
        return snack;
    }

    public void setSnack(Snack snack) {
        this.snack = snack;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SnackRank getRank() {
        return rank;
    }

    public void setRank(SnackRank rank) {
        this.rank = rank;
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
        return String.format("UserSnackRank(rank=%s, snack=%s, user=%s %s, created=%s, updated=%s)", rank, snack.getName(), user.getFirstName(), user.getLastName(), createdAt, updatedAt);
    }


    /**
     * Special ID class to represent the composite ID for this table
     */
    @Embeddable
    public static class UserSnackRankingId implements Serializable {
        private Long userId;
        private Long snackId;

        //-------------------------------------------
        // Constructors
        //
        public UserSnackRankingId() {}

        public UserSnackRankingId(Long userId, Long snackId) {
            this.userId = userId;
            this.snackId = snackId;
        }

        //-------------------------------------------
        // Getters and Setters
        //
        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Long getSnackId() {
            return snackId;
        }

        public void setSnackId(Long snackId) {
            this.snackId = snackId;
        }
    }



}
