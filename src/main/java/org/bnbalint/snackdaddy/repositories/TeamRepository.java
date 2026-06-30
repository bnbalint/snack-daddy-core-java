package org.bnbalint.snackdaddy.repositories;

import org.bnbalint.snackdaddy.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
}
