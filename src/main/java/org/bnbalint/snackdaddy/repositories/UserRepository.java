package org.bnbalint.snackdaddy.repositories;

import org.bnbalint.snackdaddy.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u JOIN u.teams t WHERE t.id = :teamId")
    List<User> findUsersByTeamId(@Param("teamId") Long teamId);
}
