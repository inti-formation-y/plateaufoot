package fr.formation.inti.repository;

import fr.formation.inti.domain.Club;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Club entity.
 */
@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {

    @Query(value = "select distinct club from Club club left join fetch club.categories",
        countQuery = "select count(distinct club) from Club club")
    Page<Club> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct club from Club club left join fetch club.categories")
    List<Club> findAllWithEagerRelationships();

    @Query("select club from Club club left join fetch club.categories where club.id =:id")
    Optional<Club> findOneWithEagerRelationships(@Param("id") Long id);
}
