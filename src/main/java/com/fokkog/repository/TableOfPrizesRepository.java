package com.fokkog.repository;

import com.fokkog.domain.TableOfPrizes;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for the TableOfPrizes entity.
 */
@Repository
public interface TableOfPrizesRepository extends JpaRepository<TableOfPrizes, Long> {

    Page<TableOfPrizes> findAllByUserId(Pageable pageable, String userId);

    Page<TableOfPrizes> findAll(Pageable pageable);
}
