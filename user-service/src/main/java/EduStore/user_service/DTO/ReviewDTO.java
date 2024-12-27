package EduStore.user_service.DTO;

import EduStore.user_service.entity.Book;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewDTO {
    private Long id;
    private Book book;
    private String content;
    private String author;
    private LocalDateTime createdAt;

}
