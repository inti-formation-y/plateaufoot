package fr.formation.inti.repository;

import fr.formation.inti.domain.Referent;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Referent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReferentRepository extends JpaRepository<Referent, Long> {
}
