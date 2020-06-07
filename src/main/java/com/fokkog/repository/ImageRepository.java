package com.fokkog.repository;

import com.fokkog.domain.Image;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Image entity.
 */
@Repository
public interface ImageRepository extends JpaRepository<Image, Long>, JpaSpecificationExecutor<Image> {

    @Query("select image from Image image where image.user.login = ?#{principal.attributes[\"sub\"]}")
    Page<Image> findByUserIsCurrentUser(Pageable pageable);
}
