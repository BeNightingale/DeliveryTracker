package track.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication//(exclude = {UserDetailsServiceAutoConfiguration.class})//,  SecurityFilterAutoConfiguration.class})//SecurityAutoConfiguration.class })//(exclude = {DefaultLoginPageConfigurer.class})
public class DeliveryTrackingApplication {

    public static void main(String[] args) {
       //   System.out.println(new BCryptPasswordEncoder().encode("olek"));

        SpringApplication.run(DeliveryTrackingApplication.class, args);
    }
}