package org.bnbalint.snackdaddy.repositories;

import jakarta.transaction.Transactional;
import org.bnbalint.snackdaddy.models.Snack;
import org.bnbalint.snackdaddy.models.SnackRank;
import org.bnbalint.snackdaddy.models.User;
import org.bnbalint.snackdaddy.models.UserSnackRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSnackRankingRepository extends JpaRepository<UserSnackRanking, Long> {

    List<UserSnackRanking> findAllByUserId(Long userId);

    @Modifying
    @Query("INSERT INTO UserSnackRanking (user, snack, rank) VALUES (:user, :snack, :rank) ON CONFLICT(id) DO NOTHING")
    int saveOnConflictDoNothing(@Param("user") User user, @Param("snack") Snack snack, @Param("rank") SnackRank rank);
}
