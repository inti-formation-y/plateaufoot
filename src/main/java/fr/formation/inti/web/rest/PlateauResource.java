package fr.formation.inti.web.rest;

import fr.formation.inti.domain.Plateau;
import fr.formation.inti.repository.PlateauRepository;
import fr.formation.inti.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link fr.formation.inti.domain.Plateau}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PlateauResource {

    private final Logger log = LoggerFactory.getLogger(PlateauResource.class);

    private static final String ENTITY_NAME = "plateau";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlateauRepository plateauRepository;

    public PlateauResource(PlateauRepository plateauRepository) {
        this.plateauRepository = plateauRepository;
    }

    /**
     * {@code POST  /plateaus} : Create a new plateau.
     *
     * @param plateau the plateau to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new plateau, or with status {@code 400 (Bad Request)} if the plateau has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/plateaus")
    public ResponseEntity<Plateau> createPlateau(@RequestBody Plateau plateau) throws URISyntaxException {
        log.debug("REST request to save Plateau : {}", plateau);
        if (plateau.getId() != null) {
            throw new BadRequestAlertException("A new plateau cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Plateau result = plateauRepository.save(plateau);
        return ResponseEntity.created(new URI("/api/plateaus/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /plateaus} : Updates an existing plateau.
     *
     * @param plateau the plateau to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated plateau,
     * or with status {@code 400 (Bad Request)} if the plateau is not valid,
     * or with status {@code 500 (Internal Server Error)} if the plateau couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/plateaus")
    public ResponseEntity<Plateau> updatePlateau(@RequestBody Plateau plateau) throws URISyntaxException {
        log.debug("REST request to update Plateau : {}", plateau);
        if (plateau.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Plateau result = plateauRepository.save(plateau);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, plateau.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /plateaus} : get all the plateaus.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of plateaus in body.
     */
    @GetMapping("/plateaus")
    public ResponseEntity<List<Plateau>> getAllPlateaus(Pageable pageable) {
        log.debug("REST request to get a page of Plateaus");
        Page<Plateau> page = plateauRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /plateaus/:id} : get the "id" plateau.
     *
     * @param id the id of the plateau to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the plateau, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/plateaus/{id}")
    public ResponseEntity<Plateau> getPlateau(@PathVariable Long id) {
        log.debug("REST request to get Plateau : {}", id);
        Optional<Plateau> plateau = plateauRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(plateau);
    }

    /**
     * {@code DELETE  /plateaus/:id} : delete the "id" plateau.
     *
     * @param id the id of the plateau to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/plateaus/{id}")
    public ResponseEntity<Void> deletePlateau(@PathVariable Long id) {
        log.debug("REST request to delete Plateau : {}", id);
        plateauRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
