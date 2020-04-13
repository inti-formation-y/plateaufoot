package fr.formation.inti.web.rest;

import fr.formation.inti.domain.Referent;
import fr.formation.inti.repository.ReferentRepository;
import fr.formation.inti.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link fr.formation.inti.domain.Referent}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ReferentResource {

    private final Logger log = LoggerFactory.getLogger(ReferentResource.class);

    private static final String ENTITY_NAME = "referent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReferentRepository referentRepository;

    public ReferentResource(ReferentRepository referentRepository) {
        this.referentRepository = referentRepository;
    }

    /**
     * {@code POST  /referents} : Create a new referent.
     *
     * @param referent the referent to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new referent, or with status {@code 400 (Bad Request)} if the referent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/referents")
    public ResponseEntity<Referent> createReferent(@RequestBody Referent referent) throws URISyntaxException {
        log.debug("REST request to save Referent : {}", referent);
        if (referent.getId() != null) {
            throw new BadRequestAlertException("A new referent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Referent result = referentRepository.save(referent);
        return ResponseEntity.created(new URI("/api/referents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /referents} : Updates an existing referent.
     *
     * @param referent the referent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated referent,
     * or with status {@code 400 (Bad Request)} if the referent is not valid,
     * or with status {@code 500 (Internal Server Error)} if the referent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/referents")
    public ResponseEntity<Referent> updateReferent(@RequestBody Referent referent) throws URISyntaxException {
        log.debug("REST request to update Referent : {}", referent);
        if (referent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Referent result = referentRepository.save(referent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, referent.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /referents} : get all the referents.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of referents in body.
     */
    @GetMapping("/referents")
    public List<Referent> getAllReferents() {
        log.debug("REST request to get all Referents");
        return referentRepository.findAll();
    }

    /**
     * {@code GET  /referents/:id} : get the "id" referent.
     *
     * @param id the id of the referent to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the referent, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/referents/{id}")
    public ResponseEntity<Referent> getReferent(@PathVariable Long id) {
        log.debug("REST request to get Referent : {}", id);
        Optional<Referent> referent = referentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(referent);
    }

    /**
     * {@code DELETE  /referents/:id} : delete the "id" referent.
     *
     * @param id the id of the referent to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/referents/{id}")
    public ResponseEntity<Void> deleteReferent(@PathVariable Long id) {
        log.debug("REST request to delete Referent : {}", id);
        referentRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
