package org.bnbalint.snackdaddy.repositories;

import org.bnbalint.snackdaddy.models.UserSnackRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSnackRankingRepository extends JpaRepository<UserSnackRanking, Long> {
}
