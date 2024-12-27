package EduStore.user_service.repo;

import EduStore.user_service.entity.BookImage;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookImageRepository extends JpaRepository<BookImage, Long> {
}
