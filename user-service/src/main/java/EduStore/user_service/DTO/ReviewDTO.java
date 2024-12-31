package EduStore.user_service.DTO;

import EduStore.user_service.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    private Long review_id;
    private Book book;
    private String content;
    private String author;
    private LocalDateTime createdAt;


    public ReviewDTO(Long review_id, String content, String author, LocalDateTime createdAt){
        this.review_id = review_id;
        this.content = content;
        this.author = author;
        this.createdAt = createdAt;
    }
}
