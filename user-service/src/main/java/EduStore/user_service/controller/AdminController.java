package EduStore.user_service.controller;

import EduStore.user_service.DTO.BookDTO;
import EduStore.user_service.DTO.ReviewDTO;
import EduStore.user_service.entity.Book;
import EduStore.user_service.entity.BookImage;
import EduStore.user_service.repo.BookImageRepository;
import EduStore.user_service.repo.BookRepository;
import EduStore.user_service.service.MinioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final BookRepository bookRepository;
    private final MinioService minioService;
    private final BookImageRepository bookImageRepository;

    @PostMapping(value = "/createBook", consumes = {"multipart/form-data"})
    public ResponseEntity<String> creatNewBookToData(
            @RequestParam("book") String bookJson,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            BookDTO bookDTO = objectMapper.readValue(bookJson, BookDTO.class);

            String imageUrl = null;
            if (file != null && !file.isEmpty()) {
                imageUrl = minioService.uploadFile(file);
            }

            Book book = Book.builder()
                    .isbn(bookDTO.getIsbn())
                    .title(bookDTO.getTitle())
                    .author(bookDTO.getAuthor())
                    .description(bookDTO.getDescription())
                    .genre(bookDTO.getGenre())
                    .free(true)
                    .count(bookDTO.getCount())
                    .price(bookDTO.getPrice())
                    .year(bookDTO.getYear())
                    .publisher(bookDTO.getPublisher())
                    .imageUrl(imageUrl)
                    .availability(bookDTO.getAvailability())
                    .binding(bookDTO.getBinding())
                    .weight(bookDTO.getWeight())
                    .age_limits(bookDTO.getAge_limits())
                    .delivery_description(bookDTO.getDelivery_description())
                    .build();
            bookRepository.save(book);

            if (imageUrl != null) {
                BookImage bookImage = new BookImage();
                bookImage.setBookId(book.getBookId());
                bookImage.setImageUrl(imageUrl);
                bookImageRepository.save(bookImage);
            }
            System.out.println("Received Book: " + bookDTO);
            if (file != null) {
                System.out.println("Received File: " + file.getOriginalFilename());
            } else {
                System.out.println("No file uploaded");
            }
            return ResponseEntity.ok("Book successfully created");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error while creating book: " + e.getMessage());
        }
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
                    bookDTO.setGenre(book.getGenre());
                    bookDTO.setReviews(book.getReviews().stream()
                            .map(review -> {
                                ReviewDTO reviewDTO = new ReviewDTO();
                                reviewDTO.setId(review.getReview_id());
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

    @PutMapping(value = "/changeBookData/{bookId}", consumes = {"multipart/form-data"})
    private String changingBookData(
            @PathVariable Long bookId,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestParam("book") String bookJson
    ) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Book book = objectMapper.readValue(bookJson, Book.class);

        Book existingBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (file != null && !file.isEmpty()) {
            String oldImageUrl = existingBook.getImageUrl();
            if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
                String oldFileName = extractFileNameFromUrl(oldImageUrl);
                if (oldFileName != null && !oldFileName.isEmpty()) {
                    minioService.deleteFile(oldFileName);
                }
            }

            String newImageUrl = minioService.uploadFile(file);
            existingBook.setImageUrl(newImageUrl);
        }

        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setDescription(book.getDescription());
        existingBook.setGenre(book.getGenre());
        existingBook.setPrice(book.getPrice());
        existingBook.setYear(book.getYear());
        existingBook.setFree(book.isFree());
        existingBook.setCount(book.getCount());
        existingBook.setPublisher(book.getPublisher());
        existingBook.setAvailability(book.getAvailability());
        existingBook.setBinding(book.getBinding());
        existingBook.setWeight(book.getWeight());
        existingBook.setAge_limits(book.getAge_limits());
        existingBook.setDelivery_description(book.getDelivery_description());

        if (book.getReviews() != null) {
            existingBook.getReviews().clear();
            existingBook.getReviews().addAll(book.getReviews());
        }


        bookRepository.save(existingBook);
        return existingBook.toString();
    }

    private String extractFileNameFromUrl(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        }
        try {
            int lastSlashIndex = url.lastIndexOf('/');
            int questionMarkIndex = url.indexOf('?');
            if (lastSlashIndex == -1 || questionMarkIndex == -1) {
                return null;
            }
            String encodedFileName = url.substring(lastSlashIndex + 1, questionMarkIndex);
            return URLDecoder.decode(encodedFileName, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            throw new RuntimeException("Error while extracting file name from URL: " + e.getMessage(), e);
        }
    }

    @DeleteMapping("/deleteBookFromData/{bookId}")
    private void deleteBookFromData(
            @PathVariable Long bookId,
            @RequestPart(value = "file", required = false) MultipartFile file
            ){

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));


        String oldImageUrl = book.getImageUrl();
        if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
            String oldFileName = extractFileNameFromUrl(oldImageUrl);
            if (oldFileName != null && !oldFileName.isEmpty()) {
                minioService.deleteFile(oldFileName);
            }
        }


        List<BookImage> bookImages = bookImageRepository.findByBookId(bookId);
        if (!bookImages.isEmpty()){
            bookImageRepository.deleteAll(bookImages);
        }

        bookRepository.deleteById(bookId);
    }

}
