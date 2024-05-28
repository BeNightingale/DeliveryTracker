package track.app;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;

@Configuration
@SpringBootTest
public class TestConfig {

    @Bean
    Environment environment() {
        return new StandardEnvironment();
    }
}
