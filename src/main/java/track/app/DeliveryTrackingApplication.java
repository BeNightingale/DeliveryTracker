package track.app;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import track.app.model.Deliverer;

@Slf4j
@SpringBootApplication
public class DeliveryTrackingApplication implements CommandLineRunner {

    private static final Logger LOGGER =  LoggerFactory.getLogger(DeliveryTrackingApplication.class);
    @Autowired
    Environment environment;
//    @Autowired
//    MyClass myClass;

    public static void main(String[] args) {

        SpringApplication.run(DeliveryTrackingApplication.class, args);
        LOGGER.info("hsgdiwuw");
    }
    @Override
    public void run(String... args) {
      //  log.error("One dollar = {}", environment.getProperty("server.port"));
        Deliverer deliverer = Deliverer.INPOST;
        System.out.println("Napisz INPOST = " + environment.getProperty(deliverer.toString()) + " przetłumaczył");
     //   myClass.getSomething();
        LOGGER.info("Spring!");
    }
}
