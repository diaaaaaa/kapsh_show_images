package at.refugeescode.kapsh_show_images.repository;

import at.refugeescode.kapsh_show_images.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image,Long> {


}
