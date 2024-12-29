package EduStore.user_service.DTO;

import EduStore.user_service.entity.Review;
import lombok.Data;

import java.time.Year;
import java.util.List;

@Data
public class BookDTO {

    private Long id;
    private String isbn;
    private String title;
    private String author;
    private String description;
    private List<Review> reviews;
    private float price;
    private int year;
    private String publisher;
    private String imageUrl;
    private String availability;
    private String binding;
    private float weight;
    private int age_limits;
    private String delivery_description;

}
