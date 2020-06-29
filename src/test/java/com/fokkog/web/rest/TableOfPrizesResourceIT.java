package com.fokkog.web.rest;

import com.fokkog.TableOfPrizesApp;
import com.fokkog.config.TestSecurityConfiguration;
import com.fokkog.domain.TableOfPrizes;
import com.fokkog.domain.User;
import com.fokkog.repository.TableOfPrizesRepository;
import com.fokkog.service.TableOfPrizesService;

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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link TableOfPrizesResource} REST controller.
 */
@SpringBootTest(classes = { TableOfPrizesApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class TableOfPrizesResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private TableOfPrizesRepository tableOfPrizesRepository;

    @Autowired
    private TableOfPrizesService tableOfPrizesService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTableOfPrizesMockMvc;

    private TableOfPrizes tableOfPrizes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TableOfPrizes createEntity(EntityManager em) {
        TableOfPrizes tableOfPrizes = new TableOfPrizes()
            .name(DEFAULT_NAME);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        tableOfPrizes.setUser(user);
        return tableOfPrizes;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TableOfPrizes createUpdatedEntity(EntityManager em) {
        TableOfPrizes tableOfPrizes = new TableOfPrizes()
            .name(UPDATED_NAME);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        tableOfPrizes.setUser(user);
        return tableOfPrizes;
    }

    @BeforeEach
    public void initTest() {
        tableOfPrizes = createEntity(em);
    }

    @Test
    @Transactional
    public void createTableOfPrizes() throws Exception {
        int databaseSizeBeforeCreate = tableOfPrizesRepository.findAll().size();
        // Create the TableOfPrizes
        restTableOfPrizesMockMvc.perform(post("/api/table-of-prizes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tableOfPrizes)))
            .andExpect(status().isCreated());

        // Validate the TableOfPrizes in the database
        List<TableOfPrizes> tableOfPrizesList = tableOfPrizesRepository.findAll();
        assertThat(tableOfPrizesList).hasSize(databaseSizeBeforeCreate + 1);
        TableOfPrizes testTableOfPrizes = tableOfPrizesList.get(tableOfPrizesList.size() - 1);
        assertThat(testTableOfPrizes.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createTableOfPrizesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tableOfPrizesRepository.findAll().size();

        // Create the TableOfPrizes with an existing ID
        tableOfPrizes.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTableOfPrizesMockMvc.perform(post("/api/table-of-prizes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tableOfPrizes)))
            .andExpect(status().isBadRequest());

        // Validate the TableOfPrizes in the database
        List<TableOfPrizes> tableOfPrizesList = tableOfPrizesRepository.findAll();
        assertThat(tableOfPrizesList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = tableOfPrizesRepository.findAll().size();
        // set the field null
        tableOfPrizes.setName(null);

        // Create the TableOfPrizes, which fails.


        restTableOfPrizesMockMvc.perform(post("/api/table-of-prizes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tableOfPrizes)))
            .andExpect(status().isBadRequest());

        List<TableOfPrizes> tableOfPrizesList = tableOfPrizesRepository.findAll();
        assertThat(tableOfPrizesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTableOfPrizes() throws Exception {
        // Initialize the database
        tableOfPrizesRepository.saveAndFlush(tableOfPrizes);

        // Get all the tableOfPrizesList
        restTableOfPrizesMockMvc.perform(get("/api/table-of-prizes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tableOfPrizes.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getTableOfPrizes() throws Exception {
        // Initialize the database
        tableOfPrizesRepository.saveAndFlush(tableOfPrizes);

        // Get the tableOfPrizes
        restTableOfPrizesMockMvc.perform(get("/api/table-of-prizes/{id}", tableOfPrizes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tableOfPrizes.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }
    @Test
    @Transactional
    public void getNonExistingTableOfPrizes() throws Exception {
        // Get the tableOfPrizes
        restTableOfPrizesMockMvc.perform(get("/api/table-of-prizes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTableOfPrizes() throws Exception {
        // Initialize the database
        tableOfPrizesService.save(tableOfPrizes);

        int databaseSizeBeforeUpdate = tableOfPrizesRepository.findAll().size();

        // Update the tableOfPrizes
        TableOfPrizes updatedTableOfPrizes = tableOfPrizesRepository.findById(tableOfPrizes.getId()).get();
        // Disconnect from session so that the updates on updatedTableOfPrizes are not directly saved in db
        em.detach(updatedTableOfPrizes);
        updatedTableOfPrizes
            .name(UPDATED_NAME);

        restTableOfPrizesMockMvc.perform(put("/api/table-of-prizes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedTableOfPrizes)))
            .andExpect(status().isOk());

        // Validate the TableOfPrizes in the database
        List<TableOfPrizes> tableOfPrizesList = tableOfPrizesRepository.findAll();
        assertThat(tableOfPrizesList).hasSize(databaseSizeBeforeUpdate);
        TableOfPrizes testTableOfPrizes = tableOfPrizesList.get(tableOfPrizesList.size() - 1);
        assertThat(testTableOfPrizes.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingTableOfPrizes() throws Exception {
        int databaseSizeBeforeUpdate = tableOfPrizesRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTableOfPrizesMockMvc.perform(put("/api/table-of-prizes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tableOfPrizes)))
            .andExpect(status().isBadRequest());

        // Validate the TableOfPrizes in the database
        List<TableOfPrizes> tableOfPrizesList = tableOfPrizesRepository.findAll();
        assertThat(tableOfPrizesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTableOfPrizes() throws Exception {
        // Initialize the database
        tableOfPrizesService.save(tableOfPrizes);

        int databaseSizeBeforeDelete = tableOfPrizesRepository.findAll().size();

        // Delete the tableOfPrizes
        restTableOfPrizesMockMvc.perform(delete("/api/table-of-prizes/{id}", tableOfPrizes.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TableOfPrizes> tableOfPrizesList = tableOfPrizesRepository.findAll();
        assertThat(tableOfPrizesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
