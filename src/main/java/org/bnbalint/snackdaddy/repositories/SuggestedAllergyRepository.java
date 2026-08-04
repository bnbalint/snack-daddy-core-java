package org.bnbalint.snackdaddy.repositories;

import org.bnbalint.snackdaddy.models.SuggestedAllergy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuggestedAllergyRepository extends JpaRepository<SuggestedAllergy, Long> {
}
