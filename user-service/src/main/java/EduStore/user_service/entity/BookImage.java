package EduStore.user_service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "book_images")
public class BookImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long bookId;

    @Column(nullable = false, length = 500)
    private String imageUrl;

    @Column(nullable = false, updatable = false, length = 500)
    private LocalDateTime createdAt = LocalDateTime.now();

}

