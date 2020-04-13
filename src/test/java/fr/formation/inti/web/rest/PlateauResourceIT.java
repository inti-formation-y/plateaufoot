package fr.formation.inti.web.rest;

import fr.formation.inti.PlateaufootApp;
import fr.formation.inti.domain.Plateau;
import fr.formation.inti.repository.PlateauRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PlateauResource} REST controller.
 */
@SpringBootTest(classes = PlateaufootApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class PlateauResourceIT {

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_HEURE_DEBUT = "AAAAAAAAAA";
    private static final String UPDATED_HEURE_DEBUT = "BBBBBBBBBB";

    private static final String DEFAULT_HEURE_FIN = "AAAAAAAAAA";
    private static final String UPDATED_HEURE_FIN = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final Integer DEFAULT_NBR_EQUIPE = 1;
    private static final Integer UPDATED_NBR_EQUIPE = 2;

    @Autowired
    private PlateauRepository plateauRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlateauMockMvc;

    private Plateau plateau;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plateau createEntity(EntityManager em) {
        Plateau plateau = new Plateau()
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN)
            .heureDebut(DEFAULT_HEURE_DEBUT)
            .heureFin(DEFAULT_HEURE_FIN)
            .adresse(DEFAULT_ADRESSE)
            .nbrEquipe(DEFAULT_NBR_EQUIPE);
        return plateau;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plateau createUpdatedEntity(EntityManager em) {
        Plateau plateau = new Plateau()
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .heureDebut(UPDATED_HEURE_DEBUT)
            .heureFin(UPDATED_HEURE_FIN)
            .adresse(UPDATED_ADRESSE)
            .nbrEquipe(UPDATED_NBR_EQUIPE);
        return plateau;
    }

    @BeforeEach
    public void initTest() {
        plateau = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlateau() throws Exception {
        int databaseSizeBeforeCreate = plateauRepository.findAll().size();

        // Create the Plateau
        restPlateauMockMvc.perform(post("/api/plateaus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(plateau)))
            .andExpect(status().isCreated());

        // Validate the Plateau in the database
        List<Plateau> plateauList = plateauRepository.findAll();
        assertThat(plateauList).hasSize(databaseSizeBeforeCreate + 1);
        Plateau testPlateau = plateauList.get(plateauList.size() - 1);
        assertThat(testPlateau.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testPlateau.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testPlateau.getHeureDebut()).isEqualTo(DEFAULT_HEURE_DEBUT);
        assertThat(testPlateau.getHeureFin()).isEqualTo(DEFAULT_HEURE_FIN);
        assertThat(testPlateau.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testPlateau.getNbrEquipe()).isEqualTo(DEFAULT_NBR_EQUIPE);
    }

    @Test
    @Transactional
    public void createPlateauWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = plateauRepository.findAll().size();

        // Create the Plateau with an existing ID
        plateau.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlateauMockMvc.perform(post("/api/plateaus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(plateau)))
            .andExpect(status().isBadRequest());

        // Validate the Plateau in the database
        List<Plateau> plateauList = plateauRepository.findAll();
        assertThat(plateauList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPlateaus() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList
        restPlateauMockMvc.perform(get("/api/plateaus?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(plateau.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].heureDebut").value(hasItem(DEFAULT_HEURE_DEBUT)))
            .andExpect(jsonPath("$.[*].heureFin").value(hasItem(DEFAULT_HEURE_FIN)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].nbrEquipe").value(hasItem(DEFAULT_NBR_EQUIPE)));
    }
    
    @Test
    @Transactional
    public void getPlateau() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get the plateau
        restPlateauMockMvc.perform(get("/api/plateaus/{id}", plateau.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(plateau.getId().intValue()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.heureDebut").value(DEFAULT_HEURE_DEBUT))
            .andExpect(jsonPath("$.heureFin").value(DEFAULT_HEURE_FIN))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.nbrEquipe").value(DEFAULT_NBR_EQUIPE));
    }

    @Test
    @Transactional
    public void getNonExistingPlateau() throws Exception {
        // Get the plateau
        restPlateauMockMvc.perform(get("/api/plateaus/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlateau() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        int databaseSizeBeforeUpdate = plateauRepository.findAll().size();

        // Update the plateau
        Plateau updatedPlateau = plateauRepository.findById(plateau.getId()).get();
        // Disconnect from session so that the updates on updatedPlateau are not directly saved in db
        em.detach(updatedPlateau);
        updatedPlateau
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .heureDebut(UPDATED_HEURE_DEBUT)
            .heureFin(UPDATED_HEURE_FIN)
            .adresse(UPDATED_ADRESSE)
            .nbrEquipe(UPDATED_NBR_EQUIPE);

        restPlateauMockMvc.perform(put("/api/plateaus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPlateau)))
            .andExpect(status().isOk());

        // Validate the Plateau in the database
        List<Plateau> plateauList = plateauRepository.findAll();
        assertThat(plateauList).hasSize(databaseSizeBeforeUpdate);
        Plateau testPlateau = plateauList.get(plateauList.size() - 1);
        assertThat(testPlateau.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testPlateau.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testPlateau.getHeureDebut()).isEqualTo(UPDATED_HEURE_DEBUT);
        assertThat(testPlateau.getHeureFin()).isEqualTo(UPDATED_HEURE_FIN);
        assertThat(testPlateau.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testPlateau.getNbrEquipe()).isEqualTo(UPDATED_NBR_EQUIPE);
    }

    @Test
    @Transactional
    public void updateNonExistingPlateau() throws Exception {
        int databaseSizeBeforeUpdate = plateauRepository.findAll().size();

        // Create the Plateau

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlateauMockMvc.perform(put("/api/plateaus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(plateau)))
            .andExpect(status().isBadRequest());

        // Validate the Plateau in the database
        List<Plateau> plateauList = plateauRepository.findAll();
        assertThat(plateauList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePlateau() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        int databaseSizeBeforeDelete = plateauRepository.findAll().size();

        // Delete the plateau
        restPlateauMockMvc.perform(delete("/api/plateaus/{id}", plateau.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Plateau> plateauList = plateauRepository.findAll();
        assertThat(plateauList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
