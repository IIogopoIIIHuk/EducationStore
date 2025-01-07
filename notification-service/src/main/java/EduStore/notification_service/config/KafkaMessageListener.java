package EduStore.notification_service.config;

//import EduStore.notification_service.service.EmailService;
import EduStore.notification_service.service.MailSenderService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaMessageListener {

//    private final EmailService emailService;


    private final MailSenderService mailSender;

    @KafkaListener(topics = "book-added", groupId = "notification-service")
    public void listenBookAdded(String message) {
        log.info("Received message on topic {}: {}", "book-added", message);
        try {
            mailSender.sendEmail(
                    "djavakafka@gmail.com",
                    "Book data",
                    "Admin added a new book: " + message
            );
            log.info("Email sent successfully for message: {}", message);
        } catch (Exception e) {
            log.error("Failed to send email for message: {}", message, e);
        }
    }


    @KafkaListener(topics = "book-cart", groupId = "notification-service")
    public void listenBookCart(String message){
        log.info("Received message on topic {}: {}", "book-cart", message);
        mailSender.sendEmail(
                "djavakafka@gmail.com",
                "Cart data",
                "User added book to cart" + message
        );
        log.info("Email sent successfully for message from cart: {}", message);
    }

    @KafkaListener(topics = "book-review", groupId = "notification-service")
    public void listenBookReview(String message){
        log.info("Received message on topic {}: {}", "book-review", message);
            mailSender.sendEmail(
                    "djavakafka@gmail.com",
                    "Review data",
                    "Get a new review for book" + message
            );
        log.info("Email sent successfully for message from review: {}", message);

    }
}
