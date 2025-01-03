package EduStore.user_service.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
@AllArgsConstructor
public class KafkaManageProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String topic, String message){
        kafkaTemplate.send(topic, message);
    }
}
