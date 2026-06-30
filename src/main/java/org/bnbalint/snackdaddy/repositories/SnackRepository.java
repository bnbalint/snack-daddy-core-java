package org.bnbalint.snackdaddy.repositories;

import org.bnbalint.snackdaddy.models.Snack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SnackRepository extends JpaRepository<Snack, Long> {
}
