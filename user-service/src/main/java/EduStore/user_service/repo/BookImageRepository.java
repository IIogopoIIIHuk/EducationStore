package EduStore.user_service.repo;

import EduStore.user_service.entity.BookImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface BookImageRepository extends JpaRepository<BookImage, Long> {
    List<BookImage> findByBookId(Long bookId);
}
