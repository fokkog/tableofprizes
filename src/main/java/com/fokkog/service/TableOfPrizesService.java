package com.fokkog.service;

import com.fokkog.domain.Prize;
import com.fokkog.domain.TableOfPrizes;
import com.fokkog.repository.TableOfPrizesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/**
 * Service Implementation for managing {@link TableOfPrizes}.
 */
@Service
@Transactional
public class TableOfPrizesService {

    private final Logger log = LoggerFactory.getLogger(TableOfPrizesService.class);

    private final TableOfPrizesRepository tableOfPrizesRepository;

    private final UserService userService;

    public TableOfPrizesService(TableOfPrizesRepository tableOfPrizesRepository, UserService userService) {
        this.tableOfPrizesRepository = tableOfPrizesRepository;
        this.userService = userService;
    }

    /**
     * Save a tableOfPrizes.
     *
     * @param tableOfPrizes the entity to save.
     * @return the persisted entity.
     */
    public TableOfPrizes save(TableOfPrizes tableOfPrizes) {
        log.debug("Request to save tableOfPrizes : {}", tableOfPrizes);
        if (tableOfPrizes.getId() == null) {
            // Create: set owner
            tableOfPrizes.setUserId(userService.getCurrentUserId());
        } else {
            // Update: check owner
            findOne(tableOfPrizes.getId());
        }
        // Fix up JsonIgnore'd property
        for (Prize prize : tableOfPrizes.getPrizes()) {
        	prize.setTableOfPrizes(tableOfPrizes);
        }
        return tableOfPrizesRepository.save(tableOfPrizes);
    }

    /**
     * Get all the tableOfPrizes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TableOfPrizes> findByUserIsCurrentUser(Pageable pageable) {
        log.debug("Request to get own tableOfPrizes");
        String userId = userService.getCurrentUserId();
        return tableOfPrizesRepository.findAllByUserId(pageable, userId);
    }

    /**
     * Get one tableOfPrizes by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TableOfPrizes> findOne(Long id) {
        log.debug("Request to get tableOfPrizes : {}", id);
        Optional<TableOfPrizes> tableOfPrizes = tableOfPrizesRepository.findById(id);
        if (!tableOfPrizes.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (!tableOfPrizes.get().getUserId().equals(userService.getCurrentUserId())) {
            throw new AccessDeniedException("Not the owner");
        }
        return tableOfPrizes;
    }

    /**
     * Delete the tableOfPrizes by id.
     *
     * @param id the id of the entity.
     */
    public TableOfPrizes delete(Long id) {
        log.debug("Request to delete tableOfPrizes : {}", id);
        // Check owner
        Optional<TableOfPrizes> tableOfPrizes = findOne(id);
        tableOfPrizesRepository.deleteById(id);
        return tableOfPrizes.get();
    }

    /**
     * Get the most recent tableOfPrizes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TableOfPrizes> findRecents() {
        log.debug("Request to get recent tablesOfPrizes");
        return tableOfPrizesRepository.findAll(PageRequest.of(0, 5));
    }
}
