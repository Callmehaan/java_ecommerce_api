package callmehaan.dev.ecommerce.storage;

import callmehaan.dev.ecommerce.storage.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByUrl(String url);
}
