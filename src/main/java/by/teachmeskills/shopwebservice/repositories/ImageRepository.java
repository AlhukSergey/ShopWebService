package by.teachmeskills.shopwebservice.repositories;

import by.teachmeskills.shopwebservice.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
}
