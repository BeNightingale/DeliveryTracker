package track.app.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SendingMailService {

    private final JavaMailSender mailSender;
    @Autowired
    public SendingMailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void send(String to, String token) {
 // todo check not nulls

        final SimpleMailMessage message = new SimpleMailMessage();
        message.setSentDate(new Date());
        final String subject = "Confirm your registration in Delivery Tracking";
        final String confirmationUrl = "http://localhost:8080/delivery_tracking/confirm?token=" + token;
        final String text = "Click the link to confirm your email: " + confirmationUrl;
        message.setTo(to);
        message.setFrom("delivery.tracker.application@gmail.com");
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
