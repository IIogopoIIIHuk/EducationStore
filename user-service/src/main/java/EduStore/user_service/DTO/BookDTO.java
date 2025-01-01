package EduStore.user_service.DTO;

import EduStore.user_service.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Year;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {

    private Long id;
    private String isbn;
    private String title;
    private String author;
    private String description;
    private String genre;
    private boolean free;
    private Integer count;
    private float price;
    private int year;
    private String publisher;
    private String imageUrl;
    private String availability;
    private String binding;
    private float weight;
    private int age_limits;
    private String delivery_description;
    private List<ReviewDTO> reviews;
}
