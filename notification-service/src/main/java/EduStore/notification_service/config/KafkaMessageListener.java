package EduStore.notification_service.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaMessageListener {

    @KafkaListener(topics = "book-added", groupId = "notification-service")
    public void listenBookAdded(String message){
        log.info("Notification: " + message);

    }

    @KafkaListener(topics = "book-cart", groupId = "notification-service")
    public void listenBookCart(String message){
        log.info("Notification: " + message);

    }

    @KafkaListener(topics = "book-review", groupId = "notification-service")
    public void listenBookReview(String message){
        log.info("Notification: " + message);

    }
}
