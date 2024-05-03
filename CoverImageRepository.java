package com.api.whatsapp.repository;

public interface CoverImageRepository extends JpaRepository<CoverImage, Long> {

    Optional<CoverImage> findByUser(User user);

}
