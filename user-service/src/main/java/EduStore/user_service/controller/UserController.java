package EduStore.user_service.controller;

import EduStore.user_service.DTO.BookDTO;
import EduStore.user_service.DTO.ReviewDTO;
import EduStore.user_service.DTO.UserDTO;
import EduStore.user_service.entity.Book;
import EduStore.user_service.entity.Cart;
import EduStore.user_service.entity.Review;
import EduStore.user_service.repo.BookRepository;
import EduStore.user_service.repo.CartRepository;
import EduStore.user_service.repo.ReviewRepository;
import EduStore.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final BookRepository bookRepository;
    private final CartRepository cartRepository;
    private final ReviewRepository reviewRepository;

    @GetMapping("/getAllBooks")
    private List<Book> getAllBooksUser(){
        return bookRepository.findAll().stream()
                .map(book -> {
                    BookDTO bookDTO = new BookDTO();
                    bookDTO.setId(book.getBookId());
                    bookDTO.setTitle(book.getTitle());
                    bookDTO.setAuthor(book.getAuthor());
                    bookDTO.setDescription(book.getDescription());
                    bookDTO.setGenre(book.getGenre());
                    bookDTO.setReviews(book.getReviews().stream()
                            .map(review -> {
                                ReviewDTO reviewDTO = new ReviewDTO();
                                reviewDTO.setReview_id(review.getReview_id());
                                reviewDTO.setBook(review.getBook());
                                reviewDTO.setAuthor(review.getAuthor());
                                reviewDTO.setContent(review.getContent());
                                reviewDTO.setCreatedAt(review.getCreatedAt());
                                return review;
                            }).collect(Collectors.toList()));
                    bookDTO.setPrice(book.getPrice());
                    bookDTO.setYear(book.getYear());
                    bookDTO.setPublisher(book.getPublisher());
                    bookDTO.setImageUrl(book.getImageUrl());
                    bookDTO.setAvailability(book.getAvailability());
                    bookDTO.setBinding(book.getBinding());
                    bookDTO.setWeight(book.getWeight());
                    bookDTO.setAge_limits(book.getAge_limits());
                    bookDTO.setDelivery_description(book.getDelivery_description());
                    return book;
                }).collect(Collectors.toList());
    }

    @PostMapping("/addBookToCart/{bookId}")
    private ResponseEntity<?> addBookToCart(@PathVariable Long bookId){

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (book.getCount() <= 0){
            return ResponseEntity.badRequest().body("This book is already out of stock");
        }

        book.setCount(book.getCount() - 1);

        if (book.getCount() == 0){
            book.setFree(false);
        }

        bookRepository.save(book);

        Cart cart = Cart.builder()
                .title_book(book.getTitle())
                .build();
        cartRepository.save(cart);

        return ResponseEntity.ok("book added to cart successfully");
    }

    @PostMapping("/makeReviewToBook/{bookId}")
    private ResponseEntity<?> makeReview(@PathVariable Long bookId, @RequestBody ReviewDTO reviewDTO){
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("book not found"));

        Review review = Review.builder()
                .content(reviewDTO.getContent())
                .author(reviewDTO.getAuthor())
                .createdAt(LocalDateTime.now())
                .book(book)
                .build();

        Review savedReview = reviewRepository.save(review);

        ReviewDTO responseDTO = new ReviewDTO(
                savedReview.getReview_id(),
                savedReview.getContent(),
                savedReview.getAuthor(),
                savedReview.getCreatedAt()
        );

        return ResponseEntity.ok(responseDTO);
    }



}
