package EduStore.notification_service.test;

//import EduStore.notification_service.service.EmailService;
import EduStore.notification_service.service.MailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class TestEmail {

    private final MailSenderService mailSender;

//    private EmailService emailService;

    @GetMapping("/mail")
    public String sendTestEmail() {
        mailSender.sendEmail(
                "djavakafka@gmail.com",
                "Test Email",
                "This is a test email."
        );
        return "Test email sent successfully";
    }
}