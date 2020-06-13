package com.fokkog.web.rest;

import com.fokkog.domain.Image;
import com.fokkog.service.ImageService;
import com.fokkog.service.UserService;
import com.fokkog.web.rest.errors.BadRequestAlertException;

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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.fokkog.domain.Image}.
 */
@RestController
@RequestMapping("/api")
public class ImageResource {

    private final Logger log = LoggerFactory.getLogger(ImageResource.class);

    private static final String ENTITY_NAME = "image";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ImageService imageService;

    private final UserService userService;

    public ImageResource(ImageService imageService, UserService userService) {
        this.imageService = imageService;
        this.userService = userService;
    }

    /**
     * {@code POST  /images} : Create a new image.
     *
     * @param image the image to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new image, or with status {@code 400 (Bad Request)} if the image has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/images")
    public ResponseEntity<Image> createImage(@Valid @RequestBody Image image) throws URISyntaxException {
        log.debug("REST request to save Image : {}", image);
        if (image.getId() != null) {
            throw new BadRequestAlertException("A new image cannot already have an ID", ENTITY_NAME, "idexists");
        }
        image.setUserId(userService.getCurrentUser().getId());
        Image result = imageService.save(image);
        return ResponseEntity.created(new URI("/api/images/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /images} : Updates an existing image.
     *
     * @param image the image to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated image,
     * or with status {@code 400 (Bad Request)} if the image is not valid,
     * or with status {@code 500 (Internal Server Error)} if the image couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/images")
    public ResponseEntity<Image> updateImage(@Valid @RequestBody Image image) throws URISyntaxException {
        log.debug("REST request to update Image : {}", image);
        if (image.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Optional<Image> origImage = imageService.findOne(image.getId());
        if (!origImage.get().getUserId().equals(userService.getCurrentUser().getId())) {
        	return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Image result = imageService.save(image);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, image.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /images} : get all the images.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of images in body.
     */
    @GetMapping("/images")
    public ResponseEntity<List<Image>> getAllImages(Pageable pageable) {
        log.debug("REST request to get a page of Images");
        Page<Image> page = imageService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /images/:id} : get the "id" image.
     *
     * @param id the id of the image to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the image, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/images/{id}")
    public ResponseEntity<Image> getImage(@PathVariable Long id) {
        log.debug("REST request to get Image : {}", id);
        Optional<Image> image = imageService.findOne(id);
        if (!image.get().getUserId().equals(userService.getCurrentUser().getId())) {
        	return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return ResponseUtil.wrapOrNotFound(image);
    }

    /**
     * {@code DELETE  /images/:id} : delete the "id" image.
     *
     * @param id the id of the image to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/images/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        log.debug("REST request to delete Image : {}", id);
        Optional<Image> origImage = imageService.findOne(id);
        if (!origImage.get().getUserId().equals(userService.getCurrentUser().getId())) {
        	return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        imageService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
