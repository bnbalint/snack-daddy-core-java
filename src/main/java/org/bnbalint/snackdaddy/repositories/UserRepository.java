package org.bnbalint.snackdaddy.repositories;

import org.bnbalint.snackdaddy.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
