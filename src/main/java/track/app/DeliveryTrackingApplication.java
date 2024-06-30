package track.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.security.config.annotation.web.configurers.DefaultLoginPageConfigurer;

@SpringBootApplication//(exclude = {UserDetailsServiceAutoConfiguration.class})//,  SecurityFilterAutoConfiguration.class})//SecurityAutoConfiguration.class })//(exclude = {DefaultLoginPageConfigurer.class})
public class DeliveryTrackingApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeliveryTrackingApplication.class, args);
    }
}
