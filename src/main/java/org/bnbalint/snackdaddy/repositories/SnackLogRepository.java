package org.bnbalint.snackdaddy.repositories;

import org.bnbalint.snackdaddy.models.SnackLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SnackLogRepository extends JpaRepository<SnackLog, Long> {
}
