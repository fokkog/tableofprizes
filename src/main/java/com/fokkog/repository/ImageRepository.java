package com.fokkog.repository;

import com.fokkog.domain.Image;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Image entity.
 */
@Repository
public interface ImageRepository extends JpaRepository<Image, Long>, JpaSpecificationExecutor<Image> {

	@Query(value = "SELECT * FROM TOP_IMAGE WHERE USER_ID = (SELECT ID FROM JHI_USER WHERE LOGIN = :login)", nativeQuery = true)   
    Page<Image> findByUserIsCurrentUser(@Param("login") String login, Pageable pageable);
}
