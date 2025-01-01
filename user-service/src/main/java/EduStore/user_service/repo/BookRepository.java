package EduStore.user_service.repo;

import EduStore.user_service.entity.Book;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findBookByTitle(String title);

    List<Book> findBookByGenre(String genre);

    List<Book> findByPriceBetween(float minPrice, float maxPrice);

    List<Book> findAll(Sort sort);
}
