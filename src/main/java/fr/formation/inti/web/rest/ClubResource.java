package fr.formation.inti.web.rest;

import fr.formation.inti.domain.Authority;
import fr.formation.inti.domain.Club;
import fr.formation.inti.domain.User;
import fr.formation.inti.repository.AuthorityRepository;
import fr.formation.inti.repository.ClubRepository;
import fr.formation.inti.security.AuthoritiesConstants;
import fr.formation.inti.service.MailService;
import fr.formation.inti.service.UserService;
import fr.formation.inti.service.dto.UserDTO;
import fr.formation.inti.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * REST controller for managing {@link fr.formation.inti.domain.Club}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ClubResource {

    private final Logger log = LoggerFactory.getLogger(ClubResource.class);

    private static final String ENTITY_NAME = "club";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;
    
    @Autowired
    private UserService userService;
    @Autowired
    private MailService mailService;
    
    @Autowired
    private AuthorityRepository authorityRepository;
    
    private final ClubRepository clubRepository;

    public ClubResource(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    /**
     * {@code POST  /clubs} : Create a new club.
     *
     * @param club the club to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new club, or with status {@code 400 (Bad Request)} if the club has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/clubs")
    public ResponseEntity<Club> createClub(@Valid @RequestBody Club club) throws URISyntaxException {
        log.debug("REST request to save Club : {}", club);
        if (club.getId() != null) {
            throw new BadRequestAlertException("A new club cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Club result = clubRepository.save(club);
        UserDTO userDto = new UserDTO();
        userDto.setEmail(result.getEmail());
        userDto.setLogin("club"+result.getId());
        Set<String> authorities = new HashSet<>();
        authorities.add(AuthoritiesConstants.USER);
        userDto.setAuthorities(authorities);
        userDto.setFirstName(result.getNom());
        userDto.setLastName("CLUB");
        userDto.setActivated(true);
        userDto.setLangKey("fr");
        User user = userService.registerUser(userDto, "123456");
        mailService.sendActivationEmail(user);
        
        return ResponseEntity.created(new URI("/api/clubs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /clubs} : Updates an existing club.
     *
     * @param club the club to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated club,
     * or with status {@code 400 (Bad Request)} if the club is not valid,
     * or with status {@code 500 (Internal Server Error)} if the club couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/clubs")
    public ResponseEntity<Club> updateClub(@Valid @RequestBody Club club) throws URISyntaxException {
        log.debug("REST request to update Club : {}", club);
        if (club.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Club result = clubRepository.save(club);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, club.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /clubs} : get all the clubs.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clubs in body.
     */
    @GetMapping("/clubs")
    public ResponseEntity<List<Club>> getAllClubs(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of Clubs");
        Page<Club> page;
        if (eagerload) {
            page = clubRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = clubRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /clubs/:id} : get the "id" club.
     *
     * @param id the id of the club to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the club, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/clubs/{id}")
    public ResponseEntity<Club> getClub(@PathVariable Long id) {
        log.debug("REST request to get Club : {}", id);
        Optional<Club> club = clubRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(club);
    }

    /**
     * {@code DELETE  /clubs/:id} : delete the "id" club.
     *
     * @param id the id of the club to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/clubs/{id}")
    public ResponseEntity<Void> deleteClub(@PathVariable Long id) {
        log.debug("REST request to delete Club : {}", id);
        clubRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
