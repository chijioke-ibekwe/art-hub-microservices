package com.arthub.artservice.repository;

import com.arthub.artservice.entity.Art;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtRepository extends JpaRepository<Art, Long> {
}
