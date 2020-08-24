package com.fokkog.web.rest;

import com.fokkog.domain.TableOfPrizes;
import com.fokkog.service.TableOfPrizesService;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.fokkog.domain.TableOfPrizes}.
 */
@RestController
@RequestMapping("/api")
public class TableOfPrizesResource {

    private final Logger log = LoggerFactory.getLogger(TableOfPrizesResource.class);

    private static final String ENTITY_NAME = "tableOfPrizes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TableOfPrizesService tableOfPrizesService;

    public TableOfPrizesResource(TableOfPrizesService tableOfPrizesService) {
        this.tableOfPrizesService = tableOfPrizesService;
    }

    /**
     * {@code POST  /table-of-prizes} : Create a new tableOfPrizes.
     *
     * @param tableOfPrizes the tableOfPrizes to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tableOfPrizes, or with status {@code 400 (Bad Request)} if the tableOfPrizes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/table-of-prizes")
    public ResponseEntity<TableOfPrizes> createTableOfPrizes(@Valid @RequestBody TableOfPrizes tableOfPrizes) throws URISyntaxException {
        log.debug("REST request to save tableOfPrizes : {}", tableOfPrizes);
        if (tableOfPrizes.getId() != null) {
            throw new BadRequestAlertException("A new tableOfPrizes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TableOfPrizes result = tableOfPrizesService.save(tableOfPrizes);
        return ResponseEntity.created(new URI("/api/table-of-prizes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getName()))
            .body(result);
    }

    /**
     * {@code PUT  /table-of-prizes} : Updates an existing tableOfPrizes.
     *
     * @param tableOfPrizes the tableOfPrizes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tableOfPrizes,
     * or with status {@code 400 (Bad Request)} if the tableOfPrizes is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tableOfPrizes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/table-of-prizes")
    public ResponseEntity<TableOfPrizes> updateTableOfPrizes(@Valid @RequestBody TableOfPrizes tableOfPrizes) throws URISyntaxException {
        log.debug("REST request to update tableOfPrizes : {}", tableOfPrizes);
        if (tableOfPrizes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TableOfPrizes result = tableOfPrizesService.save(tableOfPrizes);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getName()))
            .body(result);
    }

    /**
     * {@code GET  /table-of-prizes} : get all the tableOfPrizes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tableOfPrizes in body.
     */
    @GetMapping("/table-of-prizes")
    public ResponseEntity<List<TableOfPrizes>> getOwnTableOfPrizes(Pageable pageable) {
        log.debug("REST request to get a page of tableOfPrizes");
        Page<TableOfPrizes> page = tableOfPrizesService.findByUserIsCurrentUser(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /table-of-prizes/:id} : get the "id" tableOfPrizes.
     *
     * @param id the id of the tableOfPrizes to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tableOfPrizes, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/table-of-prizes/{id}")
    public ResponseEntity<TableOfPrizes> getTableOfPrizes(@PathVariable Long id) {
        log.debug("REST request to get tableOfPrizes : {}", id);
        Optional<TableOfPrizes> tableOfPrizes = tableOfPrizesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tableOfPrizes);
    }

    /**
     * {@code DELETE  /table-of-prizes/:id} : delete the "id" tableOfPrizes.
     *
     * @param id the id of the tableOfPrizes to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/table-of-prizes/{id}")
    public ResponseEntity<Void> deleteTableOfPrizes(@PathVariable Long id) {
        log.debug("REST request to delete tableOfPrizes : {}", id);
        TableOfPrizes result = tableOfPrizesService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, result.getName())).build();
    }

    /**
     * {@code GET  /public/recent-table-of-prizes} : get some recent tablesOfPrizes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tableOfPrizes in body.
     */
    @GetMapping("public/table-of-prizes/recent")
    public ResponseEntity<List<TableOfPrizes>> getRecentTablesOfPrizes() {
        log.debug("REST request to get recent tablesOfPrizes");
        Page<TableOfPrizes> list = tableOfPrizesService.findRecents();
        return ResponseEntity.ok().body(list.getContent());
    }
}
