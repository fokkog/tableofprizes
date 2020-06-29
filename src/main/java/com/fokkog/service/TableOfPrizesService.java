package com.fokkog.service;

import com.fokkog.domain.TableOfPrizes;
import com.fokkog.repository.TableOfPrizesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link TableOfPrizes}.
 */
@Service
@Transactional
public class TableOfPrizesService {

    private final Logger log = LoggerFactory.getLogger(TableOfPrizesService.class);

    private final TableOfPrizesRepository tableOfPrizesRepository;

    public TableOfPrizesService(TableOfPrizesRepository tableOfPrizesRepository) {
        this.tableOfPrizesRepository = tableOfPrizesRepository;
    }

    /**
     * Save a tableOfPrizes.
     *
     * @param tableOfPrizes the entity to save.
     * @return the persisted entity.
     */
    public TableOfPrizes save(TableOfPrizes tableOfPrizes) {
        log.debug("Request to save TableOfPrizes : {}", tableOfPrizes);
        return tableOfPrizesRepository.save(tableOfPrizes);
    }

    /**
     * Get all the tableOfPrizes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TableOfPrizes> findAll(Pageable pageable) {
        log.debug("Request to get all TableOfPrizes");
        return tableOfPrizesRepository.findAll(pageable);
    }


    /**
     * Get one tableOfPrizes by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TableOfPrizes> findOne(Long id) {
        log.debug("Request to get TableOfPrizes : {}", id);
        return tableOfPrizesRepository.findById(id);
    }

    /**
     * Delete the tableOfPrizes by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TableOfPrizes : {}", id);

        tableOfPrizesRepository.deleteById(id);
    }
}
