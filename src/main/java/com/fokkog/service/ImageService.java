package com.fokkog.service;

import com.fokkog.domain.Image;
import com.fokkog.repository.ImageRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Image}.
 */
@Service
@Transactional
public class ImageService {

    private final Logger log = LoggerFactory.getLogger(ImageService.class);

    private final ImageRepository imageRepository;

    private final UserService userService;

    public ImageService(ImageRepository imageRepository, UserService userService) {
        this.imageRepository = imageRepository;
        this.userService = userService;
    }

    /**
     * Save a image.
     *
     * @param image the entity to save.
     * @return the persisted entity.
     */
    public Image save(Image image) {
        log.debug("Request to save image: {}", image);
        if (image.getId() == null) {
            // Create: set owner
            image.setUserId(userService.getCurrentUser().getId());
        } else {
            // Update: check owner
            findOne(image.getId());
        }
        return imageRepository.save(image);
    }

    /**
     * Get all the images.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Image> findByUserIsCurrentUser(Pageable pageable) {
        log.debug("Request to get own images");
        String login = userService.getCurrentUser().getLogin();
        return imageRepository.findByUserIsCurrentUser(login, pageable);
    }


    /**
     * Get one image by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Image> findOne(Long id) {
        log.debug("Request to get image: {}", id);
        Optional<Image> image = imageRepository.findById(id);
        if (!image.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (!image.get().getUserId().equals(userService.getCurrentUser().getId())) {
            throw new AccessDeniedException("Not the owner");
        }
        return image;
    }

    /**
     * Delete the image by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete image: {}", id);
        // Check owner
        findOne(id);
        imageRepository.deleteById(id);
    }
}
