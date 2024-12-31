package EduStore.user_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Year;
import java.util.List;

@Entity
@Data
@Table(name = "books")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookId", nullable = false, unique = true)
    private Long bookId;

    @Column(name = "isbn", nullable = false, unique = true)
    private String isbn;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "description")
    private String description;

    @Column(name = "genre")
    private String genre;

    @Column(name = "isFree")
    private boolean isFree;

    @Column(name = "counts")
    private Integer count;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    @Column(name = "price")
    private float price;

    @Column(name = "year")
    private int year;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "imageUrl", length = 500)
    private String imageUrl;

    @Column(name = "availability")
    private String availability;

    @Column(name = "binding")
    private String binding; // переплет книги

    @Column(name = "weight")
    private float weight;

    @Column(name = "age_limits")
    private int age_limits;

    @Column(name = "delivery_description")
    private String delivery_description;

    @Override
    public String toString(){
        return "Book {" +
                "id=" + bookId +
                ", isbn='" + isbn + '\'' +
                ", title='" + title +
                ", author='" + author + '\'' +
                ", description='" + description +
                ", price='" + price + '\'' +
                ", year='" + year +
                ", publisher='" + publisher + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", availability='" + availability +
                ", binding='" + binding + '\'' +
                ", weight='" + weight +
                ", age_limits='" + age_limits + '\'' +
                ", delivery_description='" + delivery_description +
                '}';
    }

}
