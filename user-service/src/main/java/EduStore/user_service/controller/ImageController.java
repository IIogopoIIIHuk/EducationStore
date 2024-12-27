package EduStore.user_service.controller;

import EduStore.user_service.entity.BookImage;
import EduStore.user_service.repo.BookImageRepository;
import EduStore.user_service.service.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/images") //переписать все потом в AdminController
public class ImageController {

    private final MinioService minioService;
    private final BookImageRepository bookImageRepository;

    @Autowired
    public ImageController(MinioService minioService, BookImageRepository bookImageRepository) {
        this.minioService = minioService;
        this.bookImageRepository = bookImageRepository;
    }

    @PostMapping("/{bookId}")
    public ResponseEntity<String> uploadImage(@PathVariable Long bookId, @RequestParam("file") MultipartFile file) {
        try {
            // Загрузка файла в MinIO
            String imageUrl = minioService.uploadFile(file);

            // Сохранение метаданных в PostgreSQL
            BookImage bookImage = new BookImage();
            bookImage.setBookId(bookId);
            bookImage.setImageUrl(imageUrl);
            bookImageRepository.save(bookImage);

            return ResponseEntity.ok(imageUrl);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Ошибка при загрузке файла: " + e.getMessage());
        }
    }
}
