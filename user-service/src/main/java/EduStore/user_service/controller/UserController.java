package EduStore.user_service.controller;

import EduStore.user_service.DTO.BookDTO;
import EduStore.user_service.DTO.ReviewDTO;
import EduStore.user_service.DTO.UserDTO;
import EduStore.user_service.config.KafkaManageProducer;
import EduStore.user_service.entity.Book;
import EduStore.user_service.entity.Cart;
import EduStore.user_service.entity.Review;
import EduStore.user_service.repo.BookRepository;
import EduStore.user_service.repo.CartRepository;
import EduStore.user_service.repo.ReviewRepository;
import EduStore.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Comparator;
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
    private final KafkaManageProducer kafkaManageProducer;

    @GetMapping("/getAllBooks")
    private List<BookDTO> getAllBooksUser() {
        return bookRepository.findAll().stream()
                .map(this::convertToBookDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/sortList")
    private List<BookDTO> getSortList(
            @RequestParam(required = false) String bookTitle,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String order
    ){
        List<Book> books = bookRepository.findAll();

        // поиск по названию книги (учитывается нижний регистр и совпадение букв в названиях)
        if (bookTitle != null && !bookTitle.isEmpty()){
            books = books.stream()
                    .filter(book -> book.getTitle().toLowerCase().contains(bookTitle.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // сортировка по цене
        if (minPrice != null && maxPrice != null){
            books = books.stream()
                    .filter(book -> {
                        double price = book.getPrice();
                        return (minPrice == null || price >= minPrice) && (maxPrice == null || price <= maxPrice);
                    })
                    .collect(Collectors.toList());
        }

        // Сортировка по возрастанию/убыванию
        if (sortBy != null) {
            Comparator<Book> comparator = switch (sortBy) {
                case "name" -> Comparator.comparing(Book::getTitle);
                case "price" -> Comparator.comparingDouble(Book::getPrice);
                default -> Comparator.comparing(Book::getBookId);
            };

            if ("desc".equalsIgnoreCase(order)) {
                comparator = comparator.reversed();
            }

            books.sort(comparator);
        } else if (sortBy == null) {
            Comparator<Book> comparator = Comparator.comparing(Book::getBookId);
            if ("desc".equalsIgnoreCase(order)){
                comparator = comparator.reversed();
            }
            books.sort(comparator);
        }


        return books.stream()
                .map(this::convertToBookDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/searchBook")
    private ResponseEntity<?> searchBookInList(@RequestParam String title){
        List<Book> bookTitleList = bookRepository.findBookByTitle(title);

        if (bookTitleList.isEmpty()){
            ResponseEntity.status(404).body("No books found with title: " + title);
        }
        List<BookDTO> bookDTOS = bookTitleList.stream()
                .map(this::convertToBookDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(bookDTOS);
    }

    @GetMapping("/searchByGenre")
    private ResponseEntity<?> searchByGenre(@RequestParam String genre){
        List<Book> bookGenreList = bookRepository.findBookByGenre(genre);

        if (bookGenreList.isEmpty()){
            ResponseEntity.status(404).body("No books found with genre: " + genre);
        }

        List<BookDTO> bookDTOs = bookGenreList.stream()
                .map(this::convertToBookDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(bookDTOs);
    }

    @GetMapping("/searchByPriceRange")
    private ResponseEntity<?> searchByPriceRange(@RequestParam float minPrice, @RequestParam float maxPrice) {
        List<Book> booksInRange = bookRepository.findByPriceBetween(minPrice, maxPrice);

        if (booksInRange.isEmpty()) {
            return ResponseEntity.status(404).body("No books found in the price range: " + minPrice + " - " + maxPrice);
        }

        List<BookDTO> bookDTOs = booksInRange.stream()
                .map(this::convertToBookDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(bookDTOs);
    }

    @GetMapping("/sortByPrice")
    private ResponseEntity<?> sortByPrice(@RequestParam(defaultValue = "asc") String order) {
        List<Book> sortedBooks;

        if ("desc".equalsIgnoreCase(order)) {
            sortedBooks = bookRepository.findAll(Sort.by(Sort.Direction.DESC, "price"));
        } else {
            sortedBooks = bookRepository.findAll(Sort.by(Sort.Direction.ASC, "price"));
        }

        List<BookDTO> bookDTOs = sortedBooks.stream()
                .map(this::convertToBookDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(bookDTOs);
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
                .book(book)
                .title_book(book.getTitle())
                .build();
        cartRepository.save(cart);

        kafkaManageProducer.sendMessage("cart-book", "Book added to cart: " + book.getTitle());
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

        kafkaManageProducer.sendMessage("book-review", "New review: " + review.getContent() + "from user: " + review.getAuthor() + "added for book: " + book.getTitle());
        return ResponseEntity.ok(responseDTO);
    }


    private BookDTO convertToBookDTO(Book book){
        List<ReviewDTO> reviewDTOs = book.getReviews().stream()
                .map(review -> new ReviewDTO(
                        review.getReview_id(),
                        review.getContent(),
                        review.getAuthor(),
                        review.getCreatedAt()
                )).collect(Collectors.toList());

        return new BookDTO(
                book.getBookId(),
                book.getIsbn(),
                book.getTitle(),
                book.getAuthor(),
                book.getDescription(),
                book.getGenre(),
                book.isFree(),
                book.getCount(),
                book.getPrice(),
                book.getYear(),
                book.getPublisher(),
                book.getImageUrl(),
                book.getAvailability(),
                book.getBinding(),
                book.getWeight(),
                book.getAge_limits(),
                book.getDelivery_description(),
                reviewDTOs
        );
    }


}
