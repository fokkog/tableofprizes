package com.fokkog.web.rest;

import com.fokkog.TableOfPrizesApp;
import com.fokkog.config.TestSecurityConfiguration;
import com.fokkog.domain.Image;
import com.fokkog.domain.User;
import com.fokkog.repository.ImageRepository;
import com.fokkog.service.ImageService;

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
 * Integration tests for the {@link ImageResource} REST controller.
 */
@SpringBootTest(classes = { TableOfPrizesApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class ImageResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restImageMockMvc;

    private Image image;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Image createEntity(EntityManager em) {
        Image image = new Image()
            .name(DEFAULT_NAME)
            .url(DEFAULT_URL);
        return image;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Image createUpdatedEntity(EntityManager em) {
        Image image = new Image()
            .name(UPDATED_NAME)
            .url(UPDATED_URL);
        return image;
    }

    @BeforeEach
    public void initTest() {
        image = createEntity(em);
    }

    @Test
    @Transactional
    public void createImage() throws Exception {
        int databaseSizeBeforeCreate = imageRepository.findAll().size();
        // Create the Image
        restImageMockMvc.perform(post("/api/images").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(image)))
            .andExpect(status().isCreated());

        // Validate the Image in the database
        List<Image> imageList = imageRepository.findAll();
        assertThat(imageList).hasSize(databaseSizeBeforeCreate + 1);
        Image testImage = imageList.get(imageList.size() - 1);
        assertThat(testImage.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testImage.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    public void createImageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = imageRepository.findAll().size();

        // Create the Image with an existing ID
        image.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restImageMockMvc.perform(post("/api/images").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(image)))
            .andExpect(status().isBadRequest());

        // Validate the Image in the database
        List<Image> imageList = imageRepository.findAll();
        assertThat(imageList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllImages() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList
        restImageMockMvc.perform(get("/api/images?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(image.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)));
    }
    
    @Test
    @Transactional
    public void getImage() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get the image
        restImageMockMvc.perform(get("/api/images/{id}", image.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(image.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL));
    }


    @Test
    @Transactional
    public void getImagesByIdFiltering() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        Long id = image.getId();

        defaultImageShouldBeFound("id.equals=" + id);
        defaultImageShouldNotBeFound("id.notEquals=" + id);

        defaultImageShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultImageShouldNotBeFound("id.greaterThan=" + id);

        defaultImageShouldBeFound("id.lessThanOrEqual=" + id);
        defaultImageShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllImagesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where name equals to DEFAULT_NAME
        defaultImageShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the imageList where name equals to UPDATED_NAME
        defaultImageShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllImagesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where name not equals to DEFAULT_NAME
        defaultImageShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the imageList where name not equals to UPDATED_NAME
        defaultImageShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllImagesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where name in DEFAULT_NAME or UPDATED_NAME
        defaultImageShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the imageList where name equals to UPDATED_NAME
        defaultImageShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllImagesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where name is not null
        defaultImageShouldBeFound("name.specified=true");

        // Get all the imageList where name is null
        defaultImageShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllImagesByNameContainsSomething() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where name contains DEFAULT_NAME
        defaultImageShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the imageList where name contains UPDATED_NAME
        defaultImageShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllImagesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where name does not contain DEFAULT_NAME
        defaultImageShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the imageList where name does not contain UPDATED_NAME
        defaultImageShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllImagesByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where url equals to DEFAULT_URL
        defaultImageShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the imageList where url equals to UPDATED_URL
        defaultImageShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllImagesByUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where url not equals to DEFAULT_URL
        defaultImageShouldNotBeFound("url.notEquals=" + DEFAULT_URL);

        // Get all the imageList where url not equals to UPDATED_URL
        defaultImageShouldBeFound("url.notEquals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllImagesByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where url in DEFAULT_URL or UPDATED_URL
        defaultImageShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the imageList where url equals to UPDATED_URL
        defaultImageShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllImagesByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where url is not null
        defaultImageShouldBeFound("url.specified=true");

        // Get all the imageList where url is null
        defaultImageShouldNotBeFound("url.specified=false");
    }
                @Test
    @Transactional
    public void getAllImagesByUrlContainsSomething() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where url contains DEFAULT_URL
        defaultImageShouldBeFound("url.contains=" + DEFAULT_URL);

        // Get all the imageList where url contains UPDATED_URL
        defaultImageShouldNotBeFound("url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllImagesByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where url does not contain DEFAULT_URL
        defaultImageShouldNotBeFound("url.doesNotContain=" + DEFAULT_URL);

        // Get all the imageList where url does not contain UPDATED_URL
        defaultImageShouldBeFound("url.doesNotContain=" + UPDATED_URL);
    }


    @Test
    @Transactional
    public void getAllImagesByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        image.setUser(user);
        imageRepository.saveAndFlush(image);
        String userId = user.getId();

        // Get all the imageList where user equals to userId
        defaultImageShouldBeFound("userId.equals=" + userId);

        // Get all the imageList where user equals to userId + 1
        defaultImageShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultImageShouldBeFound(String filter) throws Exception {
        restImageMockMvc.perform(get("/api/images?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(image.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)));

        // Check, that the count call also returns 1
        restImageMockMvc.perform(get("/api/images/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultImageShouldNotBeFound(String filter) throws Exception {
        restImageMockMvc.perform(get("/api/images?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restImageMockMvc.perform(get("/api/images/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingImage() throws Exception {
        // Get the image
        restImageMockMvc.perform(get("/api/images/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateImage() throws Exception {
        // Initialize the database
        imageService.save(image);

        int databaseSizeBeforeUpdate = imageRepository.findAll().size();

        // Update the image
        Image updatedImage = imageRepository.findById(image.getId()).get();
        // Disconnect from session so that the updates on updatedImage are not directly saved in db
        em.detach(updatedImage);
        updatedImage
            .name(UPDATED_NAME)
            .url(UPDATED_URL);

        restImageMockMvc.perform(put("/api/images").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedImage)))
            .andExpect(status().isOk());

        // Validate the Image in the database
        List<Image> imageList = imageRepository.findAll();
        assertThat(imageList).hasSize(databaseSizeBeforeUpdate);
        Image testImage = imageList.get(imageList.size() - 1);
        assertThat(testImage.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testImage.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    public void updateNonExistingImage() throws Exception {
        int databaseSizeBeforeUpdate = imageRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImageMockMvc.perform(put("/api/images").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(image)))
            .andExpect(status().isBadRequest());

        // Validate the Image in the database
        List<Image> imageList = imageRepository.findAll();
        assertThat(imageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteImage() throws Exception {
        // Initialize the database
        imageService.save(image);

        int databaseSizeBeforeDelete = imageRepository.findAll().size();

        // Delete the image
        restImageMockMvc.perform(delete("/api/images/{id}", image.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Image> imageList = imageRepository.findAll();
        assertThat(imageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
