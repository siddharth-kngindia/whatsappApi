package com.api.whatsapp.repository;

import com.api.whatsapp.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Profile findByUserId(Long userId);

    Optional<Profile> findByEmail(String email);
}
