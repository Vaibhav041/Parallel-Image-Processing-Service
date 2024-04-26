package dev.vaibhav.imageprocessing.repository;

import dev.vaibhav.imageprocessing.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}
