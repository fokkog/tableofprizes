package com.fokkog.repository;

import com.fokkog.domain.TableOfPrizes;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the TableOfPrizes entity.
 */
@Repository
public interface TableOfPrizesRepository extends JpaRepository<TableOfPrizes, Long> {

    @Query("select tableOfPrizes from TableOfPrizes tableOfPrizes where tableOfPrizes.user.login = ?#{principal.preferredUsername}")
    List<TableOfPrizes> findByUserIsCurrentUser();
}
