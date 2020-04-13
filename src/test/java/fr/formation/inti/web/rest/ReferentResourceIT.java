package fr.formation.inti.web.rest;

import fr.formation.inti.PlateaufootApp;
import fr.formation.inti.domain.Referent;
import fr.formation.inti.repository.ReferentRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ReferentResource} REST controller.
 */
@SpringBootTest(classes = PlateaufootApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class ReferentResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_LICENCE = "AAAAAAAAAA";
    private static final String UPDATED_LICENCE = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    @Autowired
    private ReferentRepository referentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReferentMockMvc;

    private Referent referent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Referent createEntity(EntityManager em) {
        Referent referent = new Referent()
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .licence(DEFAULT_LICENCE)
            .telephone(DEFAULT_TELEPHONE)
            .email(DEFAULT_EMAIL);
        return referent;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Referent createUpdatedEntity(EntityManager em) {
        Referent referent = new Referent()
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .licence(UPDATED_LICENCE)
            .telephone(UPDATED_TELEPHONE)
            .email(UPDATED_EMAIL);
        return referent;
    }

    @BeforeEach
    public void initTest() {
        referent = createEntity(em);
    }

    @Test
    @Transactional
    public void createReferent() throws Exception {
        int databaseSizeBeforeCreate = referentRepository.findAll().size();

        // Create the Referent
        restReferentMockMvc.perform(post("/api/referents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(referent)))
            .andExpect(status().isCreated());

        // Validate the Referent in the database
        List<Referent> referentList = referentRepository.findAll();
        assertThat(referentList).hasSize(databaseSizeBeforeCreate + 1);
        Referent testReferent = referentList.get(referentList.size() - 1);
        assertThat(testReferent.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testReferent.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testReferent.getLicence()).isEqualTo(DEFAULT_LICENCE);
        assertThat(testReferent.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testReferent.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    public void createReferentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = referentRepository.findAll().size();

        // Create the Referent with an existing ID
        referent.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReferentMockMvc.perform(post("/api/referents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(referent)))
            .andExpect(status().isBadRequest());

        // Validate the Referent in the database
        List<Referent> referentList = referentRepository.findAll();
        assertThat(referentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllReferents() throws Exception {
        // Initialize the database
        referentRepository.saveAndFlush(referent);

        // Get all the referentList
        restReferentMockMvc.perform(get("/api/referents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(referent.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].licence").value(hasItem(DEFAULT_LICENCE)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }
    
    @Test
    @Transactional
    public void getReferent() throws Exception {
        // Initialize the database
        referentRepository.saveAndFlush(referent);

        // Get the referent
        restReferentMockMvc.perform(get("/api/referents/{id}", referent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(referent.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.licence").value(DEFAULT_LICENCE))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    @Transactional
    public void getNonExistingReferent() throws Exception {
        // Get the referent
        restReferentMockMvc.perform(get("/api/referents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReferent() throws Exception {
        // Initialize the database
        referentRepository.saveAndFlush(referent);

        int databaseSizeBeforeUpdate = referentRepository.findAll().size();

        // Update the referent
        Referent updatedReferent = referentRepository.findById(referent.getId()).get();
        // Disconnect from session so that the updates on updatedReferent are not directly saved in db
        em.detach(updatedReferent);
        updatedReferent
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .licence(UPDATED_LICENCE)
            .telephone(UPDATED_TELEPHONE)
            .email(UPDATED_EMAIL);

        restReferentMockMvc.perform(put("/api/referents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedReferent)))
            .andExpect(status().isOk());

        // Validate the Referent in the database
        List<Referent> referentList = referentRepository.findAll();
        assertThat(referentList).hasSize(databaseSizeBeforeUpdate);
        Referent testReferent = referentList.get(referentList.size() - 1);
        assertThat(testReferent.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testReferent.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testReferent.getLicence()).isEqualTo(UPDATED_LICENCE);
        assertThat(testReferent.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testReferent.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void updateNonExistingReferent() throws Exception {
        int databaseSizeBeforeUpdate = referentRepository.findAll().size();

        // Create the Referent

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReferentMockMvc.perform(put("/api/referents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(referent)))
            .andExpect(status().isBadRequest());

        // Validate the Referent in the database
        List<Referent> referentList = referentRepository.findAll();
        assertThat(referentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteReferent() throws Exception {
        // Initialize the database
        referentRepository.saveAndFlush(referent);

        int databaseSizeBeforeDelete = referentRepository.findAll().size();

        // Delete the referent
        restReferentMockMvc.perform(delete("/api/referents/{id}", referent.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Referent> referentList = referentRepository.findAll();
        assertThat(referentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
