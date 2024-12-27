package EduStore.user_service.controller;

import EduStore.user_service.DTO.BookDTO;
import EduStore.user_service.DTO.ReviewDTO;
import EduStore.user_service.DTO.UserDTO;
import EduStore.user_service.entity.Book;
import EduStore.user_service.repo.BookRepository;
import EduStore.user_service.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.simpleframework.xml.core.Validate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final BookRepository bookRepository;


    @PostMapping("/createBook")
    private ResponseEntity<?> creatNewBookToData(@RequestBody BookDTO bookDTO){
        Book book = Book.builder()
                .isbn(bookDTO.getIsbn())
                .title(bookDTO.getTitle())
                .author(bookDTO.getAuthor())
                .description(bookDTO.getDescription())
                .price(bookDTO.getPrice())
                .year(bookDTO.getYear())
                .publisher(bookDTO.getPublisher())
                // imageUrl
                .availability(bookDTO.getAvailability())
                .binding(bookDTO.getBinding())
                .weight(bookDTO.getWeight())
                .age_limits(bookDTO.getAge_limits())
                .delivery_description(bookDTO.getDelivery_description())
                .build();
        book = bookRepository.save(book);

        return ResponseEntity.ok("Book successfully creating");
    }

    @GetMapping("/getAllBooks")
    private List<Book> getAllBooksData(){
        return bookRepository.findAll().stream()
                .map(book -> {
                    BookDTO bookDTO = new BookDTO();
                    bookDTO.setId(book.getBookId());
                    bookDTO.setTitle(book.getTitle());
                    bookDTO.setAuthor(book.getAuthor());
                    bookDTO.setDescription(book.getDescription());
                    bookDTO.setReviews(book.getReviews().stream()
                            .map(review -> {
                                ReviewDTO reviewDTO = new ReviewDTO();
                                reviewDTO.setId(review.getId());
                                reviewDTO.setBook(review.getBook());
                                reviewDTO.setAuthor(review.getAuthor());
                                reviewDTO.setContent(review.getContent());
                                reviewDTO.setCreatedAt(review.getCreatedAt());
                                return review;
                            }).collect(Collectors.toList()));
                    bookDTO.setPrice(book.getPrice());
                    bookDTO.setYear(book.getYear());
                    bookDTO.setPublisher(book.getPublisher());
                    // imageUrl
                    bookDTO.setAvailability(book.getAvailability());
                    bookDTO.setBinding(book.getBinding());
                    bookDTO.setWeight(book.getWeight());
                    bookDTO.setAge_limits(book.getAge_limits());
                    bookDTO.setDelivery_description(book.getDelivery_description());
                    return book;
                }).collect(Collectors.toList());
    }

    @PutMapping("/changeBookData")
    private String changingBookData(@RequestParam Long id, @RequestBody Book book){
        if (!bookRepository.existsById(book.getBookId())){
            return "Book not found";
        }
        return bookRepository.save(book).toString();
    }


    @DeleteMapping("/deleteBookFromData")
    private void deleteBookFromData(@RequestParam Long bookId){
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        bookRepository.deleteById(bookId);
    }
    
}
